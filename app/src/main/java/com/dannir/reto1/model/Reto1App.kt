package com.dannir.reto1.model

import android.app.Application
import com.dannir.reto1.model.utils.SharedPreferences

class Reto1App: Application() {
    companion object{
        lateinit var prefs : SharedPreferences
    }
    override fun onCreate() {
        super.onCreate()

        prefs=SharedPreferences(applicationContext)
        prefs.createUser()
    }
}