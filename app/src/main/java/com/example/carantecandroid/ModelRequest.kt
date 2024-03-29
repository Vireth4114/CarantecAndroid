package com.example.carantecandroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ModelRequest: ViewModel() {
    private val liveData: MutableLiveData<String> = MutableLiveData<String>()

    fun getResult() : LiveData<String> = liveData

    fun doRequest(): LiveData<String> {
        Thread {
            val urlRequest = URL("https://dev-sae301grp5.users.info.unicaen.fr/api/members")
            try {
                val conn = urlRequest.openConnection() as HttpsURLConnection
                conn.connect()
                if (conn.responseCode != 200) {
                    liveData.postValue("Http error in connection (error code : ${conn.responseCode})")
                    return@Thread
                }
                val flux: BufferedReader = conn.inputStream.bufferedReader()
                liveData.postValue(flux.readText())
            } catch (e: Exception) {
                liveData.postValue("Network error during process ($e)")
                return@Thread
            }
        }.start()
        return liveData
    }
}