package com.rudyrachman16.dailytriviaquiz

import android.app.Application
import com.rudyrachman16.back_end.utils.SaveAnswer

class ApplicationSetting : Application() {
    override fun onCreate() {
        super.onCreate()
        val save = SaveAnswer(applicationContext)
        save.putAnswers("")
    }
}