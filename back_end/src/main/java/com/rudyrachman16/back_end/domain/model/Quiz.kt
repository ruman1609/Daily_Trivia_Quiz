package com.rudyrachman16.back_end.domain.model

data class Quiz(
    val difficulty: String? = null,
    val question: String,
    val correctAnswer: String,
    val answers: List<String>,
    val category: String? = null,
    val type: String
)
