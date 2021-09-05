package com.rudyrachman16.dailytriviaquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rudyrachman16.back_end.di.Injection
import com.rudyrachman16.back_end.domain.usecase.IUseCase
import com.rudyrachman16.dailytriviaquiz.ui.quiz.normal.NormalQuizViewModel

class ViewModelFactory
private constructor(private val useCase: IUseCase) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @JvmStatic
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(useCase: IUseCase = Injection.provideUseCase()) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(useCase).apply {
                    INSTANCE = this
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(NormalQuizViewModel::class.java) ->
                NormalQuizViewModel(useCase) as T
            else -> throw(Throwable("Unknown Class ${modelClass.name}"))
        }
}