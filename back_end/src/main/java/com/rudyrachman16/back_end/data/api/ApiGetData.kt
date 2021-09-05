package com.rudyrachman16.back_end.data.api

import com.rudyrachman16.back_end.data.api.model.ListQuizzesResponse
import com.rudyrachman16.back_end.data.api.retrofit.ApiRequest
import com.rudyrachman16.back_end.data.api.retrofit.ApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ApiGetData(private val apiRequest: ApiRequest) {
    companion object {
        @JvmStatic
        private var INSTANCE: ApiGetData? = null

        fun getInstance(apiRequest: ApiRequest): ApiGetData =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiGetData(apiRequest).apply { INSTANCE = this }
            }
    }

    suspend fun getQuiz(amount: Int): Flow<ApiStatus<ListQuizzesResponse>> = flow {
        try {
            val result = apiRequest.getQuiz(amount)
            if (result.results!!.isEmpty()) emit(ApiStatus.Empty)
            else emit(ApiStatus.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ApiStatus.Failed(e.message!!))
        }
    }.flowOn(Dispatchers.IO)
}