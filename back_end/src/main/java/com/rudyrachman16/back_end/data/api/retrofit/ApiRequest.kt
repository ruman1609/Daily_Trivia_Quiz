package com.rudyrachman16.back_end.data.api.retrofit

import com.rudyrachman16.back_end.data.api.model.ListQuizzesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {
    @GET("/api.php")
    suspend fun getQuiz(@Query("amount") amount: Int): ListQuizzesResponse
}