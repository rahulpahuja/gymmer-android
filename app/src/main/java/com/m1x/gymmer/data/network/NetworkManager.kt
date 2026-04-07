package com.m1x.gymmer.data.network

import com.m1x.gymmer.data.utils.LogManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManager(private val logManager: LogManager) {

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        logManager.info(LogManager.LogCategory.NETWORK, message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.gymmer.com/v1/") // Placeholder BASE_URL
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
