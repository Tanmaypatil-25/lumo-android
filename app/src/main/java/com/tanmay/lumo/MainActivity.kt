package com.tanmay.lumo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tanmay.lumo.data.remote.RetrofitClient
import com.tanmay.lumo.ui.theme.LumoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LumoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    StatusScreen()
                }
            }
        }
    }
}

@Composable
fun StatusScreen() {

    var status by remember { mutableStateOf("Tap the button") }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = status,
                style = MaterialTheme.typography.headlineSmall
            )

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val response =
                                RetrofitClient.api.getServerStatus()

                            if (response.isSuccessful) {
                                status =
                                    response.body() ?: "Empty response"
                            } else {
                                status = "Error ${response.code()}"
                            }

                        } catch (e: Exception) {
                            status = e.message ?: "Network Error"
                        }
                    }
                }
            ) {
                Text("Connect to Lumo Backend")
            }
        }
    }
}