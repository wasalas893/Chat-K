package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapplication.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    //login btn
    fun loginLoginBtnClicked(view:View){

    }
    //signup btn
    fun loginCreateUserBtnClicked(view: View){
        val createUserIntent=Intent(this,CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()

    }
}
