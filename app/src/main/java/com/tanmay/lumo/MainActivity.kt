package com.tanmay.lumo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tanmay.lumo.ui.auth.AuthViewModel
import com.tanmay.lumo.ui.auth.LoginScreen
import com.tanmay.lumo.ui.theme.LumoTheme
import com.tanmay.lumo.data.local.SessionManager
import com.tanmay.lumo.ui.auth.AuthViewModelFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.tanmay.lumo.ui.auth.AuthenticatedScreen
import com.tanmay.lumo.data.remote.RetrofitClient
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tanmay.lumo.ui.auth.SignupScreen
import com.tanmay.lumo.ui.chat.ChatViewModel
import com.tanmay.lumo.ui.chat.HomeScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitClient.initialize(applicationContext)

        setContent {

            LumoTheme {

                var showSignup by remember {
                    mutableStateOf(false)
                }

                val sessionManager = SessionManager(applicationContext)

                val isLoggedIn by sessionManager.isLoggedIn.collectAsState(
                    initial = false
                )

                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(sessionManager)
                )

                val chatViewModel: ChatViewModel = viewModel()

                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn) {
                        authViewModel.checkAuth()
                    }
                }

                if (isLoggedIn) {
                    HomeScreen(
                        chatViewModel = chatViewModel,
                        onLogout = {
                            authViewModel.logout()
                        }
                    )
                } else {

                    if (showSignup) {
                        SignupScreen(
                            viewModel = authViewModel,
                            onLoginClick = {
                                showSignup = false
                            }
                        )
                    } else {
                        LoginScreen(
                            viewModel = authViewModel,
                            onSignupClick = {
                                showSignup = true
                            }
                        )
                    }
                }
            }
        }
    }
}