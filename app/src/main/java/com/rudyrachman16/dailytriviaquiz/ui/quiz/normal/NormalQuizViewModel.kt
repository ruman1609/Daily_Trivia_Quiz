package com.rudyrachman16.dailytriviaquiz.ui.quiz.normal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rudyrachman16.back_end.data.Status
import com.rudyrachman16.back_end.domain.model.ListQuizzes
import com.rudyrachman16.back_end.domain.usecase.IUseCase

class NormalQuizViewModel(private val useCase: IUseCase) : ViewModel() {
    fun getQuiz(amount: Int = 10): LiveData<Status<ListQuizzes>?> =
        useCase.getQuiz(amount).asLiveData()

    fun resetDefault() = useCase.resetDefault()
}