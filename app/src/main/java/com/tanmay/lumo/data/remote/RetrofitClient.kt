package com.tanmay.lumo.data.remote

import android.content.Context
import com.tanmay.lumo.BuildConfig
import com.tanmay.lumo.data.local.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {

    private const val BASE_URL = BuildConfig.BASE_URL

    private lateinit var apiService: ApiService

    val api: ApiService
        get() = apiService

    fun initialize(context: Context) {

        val sessionManager = SessionManager(
            context.applicationContext
        )

        val authInterceptor = AuthInterceptor(
            sessionManager
        )

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                ScalarsConverterFactory.create()
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
    }
}