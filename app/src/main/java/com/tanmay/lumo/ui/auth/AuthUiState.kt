package com.tanmay.lumo.ui.auth

import com.tanmay.lumo.data.model.AuthResponse

sealed class AuthUiState {

    object Idle : AuthUiState()

    object Loading : AuthUiState()

    data class Success(
        val authResponse: AuthResponse
    ) : AuthUiState()

    data class Error(
        val message: String
    ) : AuthUiState()
}