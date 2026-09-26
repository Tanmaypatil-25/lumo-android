package com.tanmay.lumo.data.remote

import com.tanmay.lumo.data.model.AuthResponse
import com.tanmay.lumo.data.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.tanmay.lumo.data.model.CheckAuthResponse

interface ApiService {

    @GET("api/status")
    suspend fun getServerStatus(): Response<String>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @GET("api/auth/check")
    suspend fun checkAuth(): Response<CheckAuthResponse>

}