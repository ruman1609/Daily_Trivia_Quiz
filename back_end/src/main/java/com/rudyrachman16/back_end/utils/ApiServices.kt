package com.rudyrachman16.back_end.utils

import com.rudyrachman16.back_end.BuildConfig
import com.rudyrachman16.back_end.data.api.retrofit.ApiRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServices {
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        )
    }.build()

    val apiReq: ApiRequest by lazy {
        Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            baseUrl("https://opentdb.com")
            client(client)
        }.build().create(ApiRequest::class.java)
    }
}