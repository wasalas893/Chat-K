package com.example.myapplication

import android.app.Application
import com.example.myapplication.Utilities.SharedPrefs

class App:Application() {

    companion object{
        lateinit var prefs:SharedPrefs
    }

    override fun onCreate() {
        prefs=SharedPrefs(applicationContext)
        super.onCreate()
    }
}