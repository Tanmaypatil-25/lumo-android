package com.tanmay.lumo.data.remote

import com.tanmay.lumo.data.model.AuthResponse
import com.tanmay.lumo.data.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.tanmay.lumo.data.model.CheckAuthResponse
import com.tanmay.lumo.data.model.SignupRequest
import com.tanmay.lumo.data.model.SidebarUsersResponse
import com.tanmay.lumo.data.model.MessagesResponse
import retrofit2.http.Path
import retrofit2.http.Query
import com.tanmay.lumo.data.model.SendMessageResponse
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {

    @GET("api/status")
    suspend fun getServerStatus(): Response<String>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @GET("api/auth/check")
    suspend fun checkAuth(): Response<CheckAuthResponse>

    @GET("api/messages/users")
    suspend fun getUsers(): Response<SidebarUsersResponse>

    @GET("api/messages/{userId}")
    suspend fun getMessages(
        @Path("userId") userId: String,
        @Query("limit") limit: Int = 30
    ): Response<MessagesResponse>

    @POST("api/auth/signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): Response<AuthResponse>

    @PUT("api/messages/mark/{messageId}")
    suspend fun markMessageAsSeen(
        @Path("messageId") messageId: String
    ): Response<Unit>

    @Multipart
    @POST("api/messages/send/{userId}")
    suspend fun sendMessage(
        @Path("userId") userId: String,
        @Part("text") text: RequestBody
    ): Response<SendMessageResponse>

}