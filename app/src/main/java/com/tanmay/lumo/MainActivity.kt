package com.tanmay.lumo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tanmay.lumo.ui.auth.AuthViewModel
import com.tanmay.lumo.ui.auth.LoginScreen
import com.tanmay.lumo.ui.theme.LumoTheme
import com.tanmay.lumo.data.local.SessionManager
import com.tanmay.lumo.ui.auth.AuthViewModelFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.tanmay.lumo.ui.auth.AuthenticatedScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            LumoTheme {

                val sessionManager = SessionManager(applicationContext)

                val isLoggedIn by sessionManager.isLoggedIn.collectAsState(
                    initial = false
                )

                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(sessionManager)
                )

                if (isLoggedIn) {
                    AuthenticatedScreen()
                } else {
                    LoginScreen(
                        viewModel = authViewModel
                    )
                }
            }
        }
    }
}