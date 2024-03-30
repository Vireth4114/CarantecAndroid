package com.example.carantecandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carantecandroid.ui.theme.CarantecAndroidTheme
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            CarantecAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button({context.startActivity(Intent(context, InsertMember::class.java))},
                            Modifier.padding(10.dp)) {
                            Text("Insérer un membre", fontSize = 20.sp)
                        }
                        val modelRequest: ModelRequest by viewModels()
                        val state = modelRequest.getResult().observeAsState()
                        modelRequest.doRequest("https://dev-sae301grp5.users.info.unicaen.fr/api/members", "GET")
                        if (state.value != null) {
                            TableMembers(json = JSONObject(state.value!!))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableMembers(json: JSONObject) {
    Row(Modifier.verticalScroll(rememberScrollState())
                .horizontalScroll(rememberScrollState())
                .padding(10.dp)) {
        val data = json.getJSONArray("data")
        ColumnAttribute(data, "id", false, "Id")
        ColumnAttribute(data, "licence", true, "Licence")
        ColumnAttribute(data, "name", true, "Nom")
        ColumnAttribute(data, "surname", true, "Prénom")
        ColumnAttribute(data, "remaining_dives", true, "Plongées Restantes")
    }
}

@Composable
fun ColumnAttribute(data: JSONArray, attribute: String, isAttribute: Boolean, title: String = attribute) {
    Column(
        Modifier.width(intrinsicSize = IntrinsicSize.Max)) {
        Text(title, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().background(Color(255, 128, 128)).border(1.dp, Color.Black).padding(5.dp))
        for (i in 0 until data.length())
            Text(if (isAttribute)
                    data.getJSONObject(i).getJSONObject("attributes").getString(attribute)
                else
                    data.getJSONObject(i).getString(attribute),
                textAlign = TextAlign.Center,
                modifier = Modifier.background(
                            if (i % 2 == 1) Color(0,0,0,20)
                            else Color.Transparent
                        )
                        .border(1.dp, Color.Black)
                        .fillMaxWidth()
                        .padding(5.dp))
    }
}