package com.tanmay.lumo.data.repository

import com.tanmay.lumo.data.model.SidebarUsersResponse
import com.tanmay.lumo.data.remote.ApiService
import retrofit2.Response
import com.tanmay.lumo.data.model.MessagesResponse
import com.tanmay.lumo.data.model.SendMessageResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class ChatRepository(
    private val apiService: ApiService
) {

    suspend fun getUsers(): Response<SidebarUsersResponse> {
        return apiService.getUsers()
    }

    suspend fun getMessages(
        userId: String
    ): Response<MessagesResponse> {

        return apiService.getMessages(
            userId = userId
        )
    }

    suspend fun sendMessage(
        userId: String,
        text: String
    ): Response<SendMessageResponse> {

        val textBody = text.toRequestBody(
            "text/plain".toMediaType()
        )

        return apiService.sendMessage(
            userId = userId,
            text = textBody
        )
    }
}