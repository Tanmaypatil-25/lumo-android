package com.tanmay.lumo.data.repository

import com.tanmay.lumo.data.model.AuthResponse
import com.tanmay.lumo.data.model.LoginRequest
import com.tanmay.lumo.data.remote.ApiService
import retrofit2.Response
import com.tanmay.lumo.data.model.CheckAuthResponse
import com.tanmay.lumo.data.model.SignupRequest

class AuthRepository(
    private val apiService: ApiService
) {

    suspend fun checkAuth(): Response<CheckAuthResponse> {
        return apiService.checkAuth()
    }

    suspend fun login(
        email: String,
        password: String
    ): Response<AuthResponse> {

        val request = LoginRequest(
            email = email,
            password = password
        )

        return apiService.login(request)
    }

    suspend fun signup(
        fullName: String,
        email: String,
        password: String,
        bio: String
    ): Response<AuthResponse> {

        val request = SignupRequest(
            fullName = fullName,
            email = email,
            password = password,
            bio = bio
        )

        return apiService.signup(request)
    }
}