package com.tanmay.lumo.data.repository

import com.tanmay.lumo.data.model.SidebarUsersResponse
import com.tanmay.lumo.data.remote.ApiService
import retrofit2.Response
import com.tanmay.lumo.data.model.MessagesResponse

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
}