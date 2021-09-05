package com.rudyrachman16.back_end.data.api.model

import com.google.gson.annotations.SerializedName

data class QuizResponse(
    val difficulty: String? = null,
    val question: String? = null,

    @SerializedName("correct_answer")
    val correctAnswer: String? = null,

    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String?>? = null,
    val category: String? = null,
    val type: String? = null
)
