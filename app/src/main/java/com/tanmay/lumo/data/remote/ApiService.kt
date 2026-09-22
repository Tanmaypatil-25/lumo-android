package com.tanmay.lumo.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api/status")
    suspend fun getServerStatus(): Response<String>
}