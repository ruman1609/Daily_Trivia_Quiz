package com.rudyrachman16.back_end.data

import com.rudyrachman16.back_end.data.api.ApiGetData
import com.rudyrachman16.back_end.data.api.retrofit.ApiStatus
import com.rudyrachman16.back_end.domain.model.ListQuizzes
import com.rudyrachman16.back_end.domain.repository.IRepository
import com.rudyrachman16.back_end.utils.MapVal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class Repository(private val apiData: ApiGetData) : IRepository {
    companion object {
        @JvmStatic
        var INSTANCE: IRepository? = null
        fun getInstance(apiData: ApiGetData): IRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(apiData).apply {
                    INSTANCE = this
                }
            }

        @JvmStatic
        private var listQuizzes: ListQuizzes? = null
    }

    override fun getQuiz(amount: Int): Flow<Status<ListQuizzes>> = flow {
        emit(Status.Loading<ListQuizzes>())
        if (listQuizzes == null) {
            when (val result = apiData.getQuiz(amount).first()) {
                is ApiStatus.Success -> {
                    listQuizzes = MapVal.listQuizApiToDom(result.data)
                    emit(Status.Success<ListQuizzes>(listQuizzes!!))
                }
                is ApiStatus.Empty -> emit(Status.Error<ListQuizzes>("Empty Value"))
                is ApiStatus.Failed -> emit(Status.Error<ListQuizzes>(result.error))
            }
        } else emit(Status.Success<ListQuizzes>(listQuizzes!!))
    }

    override fun resetDefault() {
        listQuizzes = null
    }


}