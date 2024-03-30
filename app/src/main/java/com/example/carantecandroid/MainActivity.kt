package com.example.carantecandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.carantecandroid.ui.theme.CarantecAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val modelRequest = ViewModelProvider(this)[ModelRequest::class.java]
        setContent {
            val context = LocalContext.current
            val state = modelRequest.getResult().observeAsState()
            val stateBDD = modelRequest.getRoomResult().observeAsState()
            modelRequest.doRequest("https://dev-sae301grp5.users.info.unicaen.fr/api/members", "GET")
            CarantecAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button({context.startActivity(Intent(context, InsertMember::class.java))},
                            Modifier.padding(10.dp)) {
                            Text(context.resources.getString(R.string.insert_member), fontSize = 20.sp)
                        }
                        // Try to display the API request, otherwise select in DDB
                        if (state.value != null) {
                            TableMembers(state.value!!)
                        } else {
                            if (stateBDD.value != null) {
                                TableMembers(stateBDD.value!!)
                            } else {
                                Text(context.resources.getString(R.string.loading))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableMembers(members: List<Member>) {
    val context = LocalContext.current

    Row(Modifier.verticalScroll(rememberScrollState())
                .horizontalScroll(rememberScrollState())
                .padding(10.dp)) {
        ColumnAttribute(members, "id", context.resources.getString(R.string.id))
        ColumnAttribute(members, "licence", context.resources.getString(R.string.license))
        ColumnAttribute(members, "name", context.resources.getString(R.string.name))
        ColumnAttribute(members, "surname", context.resources.getString(R.string.surname))
        ColumnAttribute(members, "dives", context.resources.getString(R.string.remaining_dives))
    }
}

@Composable
fun ColumnAttribute(members: List<Member>, attribute: String, title: String = attribute) {
    Column(
        Modifier.width(intrinsicSize = IntrinsicSize.Max)) {
        Text(title, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().background(Color(255, 128, 128)).border(1.dp, Color.Black).padding(5.dp))
        for (i in members.indices)
            Text(when (attribute) {
                     "id" -> members[i].id
                     "licence" -> members[i].licence
                     "name" -> members[i].name
                     "surname" -> members[i].surname
                     else -> members[i].dives
                }.toString(),
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
