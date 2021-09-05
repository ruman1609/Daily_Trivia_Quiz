package com.rudyrachman16.back_end.utils

import android.content.Context

class SaveAnswer(context: Context) {
    companion object {
        const val ANSWER_KEY = "com.rudyrachman16.back_end.utils.SaveAnswer"
    }

    private val preferences = context.getSharedPreferences(ANSWER_KEY, Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    fun putAnswers(answers: String) {
        editor.putString(ANSWER_KEY, answers).commit()
    }

    fun getAnswers() = preferences.getString(ANSWER_KEY, "")
}