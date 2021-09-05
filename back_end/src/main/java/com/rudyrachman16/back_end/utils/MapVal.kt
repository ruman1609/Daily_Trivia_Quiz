package com.rudyrachman16.back_end.utils

import android.os.Build
import android.text.Html
import com.rudyrachman16.back_end.data.api.model.ListQuizzesResponse
import com.rudyrachman16.back_end.domain.model.ListQuizzes
import com.rudyrachman16.back_end.domain.model.Quiz

object MapVal {
    fun listQuizApiToDom(listQuizzesRes: ListQuizzesResponse): ListQuizzes {
        val listQuiz: MutableList<Quiz> = mutableListOf()
        listQuizzesRes.results?.forEach {
            val question = htmlToString(it.question!!)
            var correctAnswer = htmlToString(it.correctAnswer!!)
            val answers: MutableList<String> = mutableListOf(correctAnswer).let { choice ->
                choice.addAll(it.incorrectAnswers!!.map { answer -> htmlToString(answer!!) })
                if ((it.type == "boolean")) {
                    correctAnswer = correctAnswer.lowercase()
                    choice.map { answer -> answer.lowercase() }.toMutableList()
                }
                choice.shuffled().toMutableList()
            }
            listQuiz.add(
                Quiz(it.difficulty, question, correctAnswer, answers, it.category, it.type!!)
            )
        }
        return ListQuizzes(listQuiz)
    }

    private fun htmlToString(source: String): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY).toString()
        else Html.fromHtml(source).toString()
}