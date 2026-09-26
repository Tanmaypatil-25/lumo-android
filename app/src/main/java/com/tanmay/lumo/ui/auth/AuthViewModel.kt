package com.tanmay.lumo.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanmay.lumo.data.remote.RetrofitClient
import com.tanmay.lumo.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.tanmay.lumo.data.local.SessionManager

class AuthViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val repository = AuthRepository(
        RetrofitClient.api
    )

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)

    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun checkAuth() {
        viewModelScope.launch {
            try {
                val response = repository.checkAuth()

                if (!response.isSuccessful) {
                    sessionManager.clearSession()
                }
            } catch (e: Exception) {
                // Don't logout on network failure
            }
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {

            _uiState.value = AuthUiState.Loading

            try {
                val response = repository.login(
                    email,
                    password
                )

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {
                        sessionManager.saveToken(body.token)
                        _uiState.value = AuthUiState.Success(body)
                    } else {
                        _uiState.value = AuthUiState.Error("Empty response from server")
                    }

                } else {
                    _uiState.value = AuthUiState.Error("Login failed")
                }

            } catch (e: Exception) {
                _uiState.value =
                    AuthUiState.Error(
                        e.message ?: "Something went wrong"
                    )
            }
        }
    }
}