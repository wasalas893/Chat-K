package com.example.myapplication.Services

import android.graphics.Color
import com.example.myapplication.App
import java.util.*


object UserDataService {
    var id=""
    var avatarColor=""
    var avatarName=""
    var email=""
    var name=""


    fun logout(){
        id=""
        avatarColor=""
        avatarName=""
        email=""
        name=""
        App.prefs.authToken=""
        App.prefs.userEmail=""
        App.prefs.isLoggedIn=false
        MessageService.clearMessages()
        MessageService.clearChannels()
    }





    fun returnAvatarColor(components: String) : Int {
        val strippedColor = components.replace("[", "").replace("]", "").replace(",", "")

        var r = 0
        var g = 0
        var b = 0

        if (Scanner(strippedColor).hasNext()) {
            r = (Scanner(strippedColor).nextDouble() * 255).toInt()
            g = (Scanner(strippedColor).nextDouble() * 255).toInt()
            b = (Scanner(strippedColor).nextDouble() * 255).toInt()
        }
        return Color.rgb(r,g,b)
    }
}