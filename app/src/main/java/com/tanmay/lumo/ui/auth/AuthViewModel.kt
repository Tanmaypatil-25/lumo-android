package com.tanmay.lumo.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanmay.lumo.data.model.AuthResponse
import com.tanmay.lumo.data.remote.RetrofitClient
import com.tanmay.lumo.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository(
        RetrofitClient.api
    )

    private val _loginResult = MutableStateFlow<AuthResponse?>(null)

    val loginResult: StateFlow<AuthResponse?> = _loginResult.asStateFlow()

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {

            val response = repository.login(
                email,
                password
            )

            if (response.isSuccessful) {
                _loginResult.value = response.body()
            }
        }
    }
}