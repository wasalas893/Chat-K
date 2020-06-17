package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapplication.R
import com.example.myapplication.Services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar="profileDefault"
    var avatarColor="[0.5,0.5,0.5,1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }
    //user avatar
    fun genarateUserAvatar(view: View){
        val random=Random()
        val color= random.nextInt(2)
        val avatar=random.nextInt(28)
        if(color==0){
            userAvatar="light$avatar"
        }else{
            userAvatar="dark$avatar"
        }
        val resourceId=resources.getIdentifier(userAvatar,"drawable",packageName)
        createAvatarimageView.setImageResource(resourceId)

    }
    //generate color btn
   fun generateColorClicked(view: View){
        val random=Random()
        val r=random.nextInt(255)
        val g=random.nextInt(255)
        val b=random.nextInt(255)
        createAvatarimageView.setBackgroundColor(Color.rgb(r,g,b))
        val savedR=r.toDouble()/255
        val savedG=g.toDouble()/255
        val savedB=b.toDouble()/255
        avatarColor="[$savedR,$savedG,$savedB,1]"
        println(avatarColor)

   }
    //create user btn
    fun createUserClicked(view: View){
        val email=createEmailText.text.toString()
        val password=createPasswordText.text.toString()

        AuthService.registerUser(this,email,password){registerSuccess->
            if (registerSuccess){
                AuthService.loginUser(this,email,password){loginSuccess->
                    if(loginSuccess){
                      println(AuthService.authToken)
                        println(AuthService.userEmail)
                    }
                }

            }
        }
   }

}


