package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapplication.R
import com.example.myapplication.Services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    //login btn
    fun loginLoginBtnClicked(view:View){
        val email=loginEmailText.text.toString()
        val password=loginPasswordText.text.toString()

        AuthService.loginUser(this,email,password){loginSuccses->
            if(loginSuccses){
                AuthService.findUserByEmail(this){findSuccess->
                    if(findSuccess){
                        finish()
                    }

                }
            }

        }

    }
    //signup btn
    fun loginCreateUserBtnClicked(view: View){
        val createUserIntent=Intent(this,CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()

    }
}
