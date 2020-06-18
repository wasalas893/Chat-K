package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.myapplication.R
import com.example.myapplication.Services.AuthService
import com.example.myapplication.Services.UserDataService
import com.example.myapplication.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)





        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_tools,
                R.id.nav_share,
                R.id.nav_send
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE)
        )





    }

    private val userDataChangeReceiver=object:BroadcastReceiver(){
        override fun onReceive(context:Context?, intent:Intent?) {
            if(AuthService.isLoggedIn){
                userNameNavHeader.text=UserDataService.name
                userEmailNavHeader.text=UserDataService.email
                val resoureId=resources.getIdentifier(UserDataService.avatarName,"drawable",
                packageName)
                userImageNavHeader.setImageResource(resoureId)
                // userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                loginBtnNavHeader.text="Logout"
            }

        }
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //login btn
    fun loginBtnNavClicked(view:View){
        if(AuthService.isLoggedIn){
            //logout
       UserDataService.logout()
            userNameNavHeader.text="Login"
            userEmailNavHeader.text=""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginBtnNavHeader.text="Login"
        }else{
            val loginIntent=Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

    }
    //add chenal
    fun addChannelClicked(view: View){

    }
    //send message
    fun sendMsgBtnClicked(view: View){

    }
}
