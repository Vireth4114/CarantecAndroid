package com.example.carantecandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carantecandroid.ui.theme.CarantecAndroidTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarantecAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column {
                        val modelRequest: ModelRequest by viewModels()
                        val state = modelRequest.getResult().observeAsState()
                        modelRequest.doRequest()
                        if (state.value != null) {
                            DetailsIP(json = JSONObject(state.value!!))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsIP(json: JSONObject) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        val data = json.getJSONArray("data")
        for (i in 0 until data.length()) {
            val members = data.getJSONObject(i)
            val attributes = members.getJSONObject("attributes")
            Row(Modifier.background(if (i%2 == 1) Color(240, 240, 240, 255) else Color.Transparent)) {
                Text(text = members.getString("id"), Modifier.width(30.dp).padding(3.dp))
                Text(text = attributes.getString("licence"), Modifier.width(150.dp).padding(3.dp).wrapContentHeight(Alignment.CenterVertically))
                Text(text = attributes.getString("name"), Modifier.width(100.dp).padding(3.dp))
                Text(text = attributes.getString("surname"), Modifier.width(100.dp).padding(3.dp))
                Text(text = attributes.getString("remaining_dives"), Modifier.width(20.dp).padding(3.dp))
            }
        }
    }
}
