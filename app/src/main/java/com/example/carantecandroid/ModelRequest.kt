package com.example.carantecandroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.BufferedReader
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.json.JSONObject

class ModelRequest(app: Application): AndroidViewModel(app) {
    val base = Base.getInstance(app)
    private val liveData: MutableLiveData<List<Member>> = MutableLiveData<List<Member>>()
    private val roomLiveData: LiveData<List<Member>> = base.memberDAO().getAll()

    fun getResult() : LiveData<List<Member>> {
        return liveData
    }
    fun getRoomResult() : LiveData<List<Member>> {
        return roomLiveData
    }

    fun doRequest(url: String, method: String): LiveData<List<Member>> {
        Thread {
            val urlRequest = URL(url)
            try {
                val conn = urlRequest.openConnection() as HttpsURLConnection
                conn.requestMethod = method
                conn.connect()
                if (conn.responseCode != 200) {
                    return@Thread
                }
                if (method == "GET") {
                    val flux: BufferedReader = conn.inputStream.bufferedReader()
                    val data = JSONObject(flux.readText()).getJSONArray("data")
                    val list: ArrayList<Member> = ArrayList()
                    for (i in 0 until data.length()) {
                        val member = data.getJSONObject(i)
                        val attribute = member.getJSONObject("attributes")
                        val newMember = Member(
                            id = member.getString("id").toLong(),
                            licence = attribute.getString("licence"),
                            name = attribute.getString("name"),
                            surname = attribute.getString("surname"),
                            dives = attribute.getString("remaining_dives").toInt(),
                            date = attribute.getString("date_certification"),
                            subdate = attribute.getString("subdate"),
                            pricing = attribute.getString("pricing")
                        )
                        list.add(newMember)
                    }

                    // Add to the API all members posted in offline
                    for (member in roomLiveData.value!!.minus(list.toSet())) {
                        doRequest("https://dev-sae301grp5.users.info.unicaen.fr/api/members?" +
                                "licence=" + member.licence + "&" +
                                "name=" + member.name + "&" +
                                "surname=" + member.surname + "&" +
                                "date_certification=" + member.date + "&" +
                                "pricing=" + member.pricing + "&" +
                                "password=" + "NotStoredInAPI:(" + "&" +
                                "subdate=" + member.subdate, "POST")
                        list.add(member)
                    }

                    // Update DDB when there is an API request

                    base.memberDAO().deleteAll()
                    for (newMember in list)
                        base.memberDAO().insertOne(newMember)
                    liveData.postValue(list)
                }
            } catch (e: Exception) {
                return@Thread
            }
        }.start()
        return liveData
    }
}