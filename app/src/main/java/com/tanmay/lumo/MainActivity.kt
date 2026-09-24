package com.tanmay.lumo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tanmay.lumo.ui.auth.AuthViewModel
import com.tanmay.lumo.ui.auth.LoginScreen
import com.tanmay.lumo.ui.theme.LumoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            LumoTheme {

                val authViewModel: AuthViewModel = viewModel()

                LoginScreen(
                    viewModel = authViewModel
                )
            }
        }
    }
}