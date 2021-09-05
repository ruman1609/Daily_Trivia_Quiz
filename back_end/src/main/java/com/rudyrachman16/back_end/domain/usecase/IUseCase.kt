package com.rudyrachman16.back_end.domain.usecase

import com.rudyrachman16.back_end.data.Status
import com.rudyrachman16.back_end.domain.model.ListQuizzes
import kotlinx.coroutines.flow.Flow

interface IUseCase {
    fun getQuiz(amount: Int): Flow<Status<ListQuizzes>>

    fun resetDefault()
}