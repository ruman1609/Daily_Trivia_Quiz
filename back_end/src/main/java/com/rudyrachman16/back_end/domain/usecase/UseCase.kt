package com.rudyrachman16.back_end.domain.usecase

import com.rudyrachman16.back_end.data.Status
import com.rudyrachman16.back_end.domain.model.ListQuizzes
import com.rudyrachman16.back_end.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow

class UseCase(private val repository: IRepository) : IUseCase {
    companion object {
        @JvmStatic
        private var INSTANCE: IUseCase? = null
        fun getInstance(repository: IRepository) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UseCase(repository).apply {
                    INSTANCE = this
                }
            }
    }

    override fun getQuiz(amount: Int): Flow<Status<ListQuizzes>> =
        repository.getQuiz(amount)

    override fun resetDefault() = repository.resetDefault()
}