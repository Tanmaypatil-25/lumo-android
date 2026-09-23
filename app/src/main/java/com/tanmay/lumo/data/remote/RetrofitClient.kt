package com.tanmay.lumo.data.remote

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.tanmay.lumo.BuildConfig
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = BuildConfig.BASE_URL

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}