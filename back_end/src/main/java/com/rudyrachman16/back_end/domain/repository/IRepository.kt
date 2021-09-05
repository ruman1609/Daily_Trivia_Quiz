package com.rudyrachman16.back_end.domain.repository

import com.rudyrachman16.back_end.data.Status
import com.rudyrachman16.back_end.domain.model.ListQuizzes
import kotlinx.coroutines.flow.Flow

interface IRepository {
    fun getQuiz(amount: Int): Flow<Status<ListQuizzes>>
    fun resetDefault()
}