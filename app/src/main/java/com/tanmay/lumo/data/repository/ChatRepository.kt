package com.tanmay.lumo.data.repository

import com.tanmay.lumo.data.model.SidebarUsersResponse
import com.tanmay.lumo.data.remote.ApiService
import retrofit2.Response

class ChatRepository(
    private val apiService: ApiService
) {

    suspend fun getUsers(): Response<SidebarUsersResponse> {
        return apiService.getUsers()
    }
}