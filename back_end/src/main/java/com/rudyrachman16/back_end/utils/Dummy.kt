package com.rudyrachman16.back_end.utils

import android.os.Build
import android.text.Html
import com.rudyrachman16.back_end.domain.model.Quiz

object Dummy {
    private const val multipleQuestion =
        "What name did &quot;Mario&quot;, from &quot;Super Mario Brothers&quot;, originally have?"
    val multiply = Quiz(
        difficulty = "medium",
        question =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(multipleQuestion, Html.FROM_HTML_MODE_LEGACY).toString()
        else Html.fromHtml(multipleQuestion).toString(),
        correctAnswer = "Ossan",
        answers = listOf("Ossan", "Jumpman", "Mr. Video", "Mario"),
        category = "Entertainment: Video Games",
        type = "multiple"
    )

    val boolean = Quiz(
        difficulty = "easy",
        question = "Water always boils at 100°C, 212°F, 373.15K, no matter where you are.",
        correctAnswer = "false",
        answers = listOf("false", "true"),
        category = "Science & Nature",
        type = "boolean"
    )
}