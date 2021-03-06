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
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapters.MessageAdapter
import com.example.myapplication.Model.Channel
import com.example.myapplication.Model.Message
import com.example.myapplication.R
import com.example.myapplication.Services.AuthService
import com.example.myapplication.Services.MessageService
import com.example.myapplication.Services.UserDataService
import com.example.myapplication.Utilities.BROADCAST_USER_DATA_CHANGE
import com.example.myapplication.Utilities.SOCKET_URL
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

     val socket=IO.socket(SOCKET_URL)
    lateinit var channelAdapter:ArrayAdapter<Channel>
    lateinit var messageAdapter:MessageAdapter
    var selectedChannel:Channel?=null

    private  fun setupAdapters(){
        channelAdapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,MessageService.channels)
        channel_list.adapter=channelAdapter

        messageAdapter= MessageAdapter(this,MessageService.messages)
        messageListView.adapter=messageAdapter
        val layoutManager=LinearLayoutManager(this)
        messageListView.layoutManager=layoutManager
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        socket.connect()
        socket.on("channelCreated",onNewChannel)
        socket.on("messageCreated",onNewMessage)




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
        hidekeyboard()
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE)
        )
       setupAdapters()

        //click the channel view page
        channel_list.setOnItemClickListener { _, _, i,_ ->
            selectedChannel=MessageService.channels[i]
            drawer_layout.closeDrawer(GravityCompat.START)
            updateWithChannel()

        }

        if(App.prefs.isLoggedIn){
            AuthService.findUserByEmail(this){

            }
        }






    }


    override fun onDestroy() {
        socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onDestroy()
    }

    private val userDataChangeReceiver=object:BroadcastReceiver(){
        override fun onReceive(context:Context, intent:Intent?) {
            if(App.prefs.isLoggedIn){
                userNameNavHeader.text=UserDataService.name
                userEmailNavHeader.text=UserDataService.email
                val resoureId=resources.getIdentifier(UserDataService.avatarName,"drawable",
                packageName)
                userImageNavHeader.setImageResource(resoureId)
               
                loginBtnNavHeader.text="Logout"

                MessageService.getChannels{complete->
                   if(complete){
                       if(MessageService.channels.count()>0){
                           selectedChannel=MessageService.channels[0]
                           channelAdapter.notifyDataSetChanged()
                           updateWithChannel()
                       }
                   }
                }
            }

        }

    }

    fun updateWithChannel(){
        mainChannelName.text="#${selectedChannel?.name}"

        //message download
        if(selectedChannel !=null){
            MessageService.getMessages(selectedChannel!!.id){complete->
                if(complete){
                   messageAdapter.notifyDataSetChanged()
                    if(messageAdapter.itemCount>0){
                        messageListView.smoothScrollToPosition(messageAdapter.itemCount-1)

                    }
                }

            }
        }


    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //login btn
    fun loginBtnNavClicked(view:View){
        if(App.prefs.isLoggedIn){
            //logout
       UserDataService.logout()
            channelAdapter.notifyDataSetChanged()
            messageAdapter.notifyDataSetChanged()
            userNameNavHeader.text=""
            userEmailNavHeader.text=""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginBtnNavHeader.text="Login"
            mainChannelName.text="Please log in"
        }else{
            val loginIntent=Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

    }
    //add chenal
    fun addChannelClicked(view: View){
        if(App.prefs.isLoggedIn){
            val builder=AlertDialog.Builder(this)
            val dialogView=layoutInflater.inflate(R.layout.add_channel_dialog,null)

             builder.setView(dialogView)
                 .setPositiveButton("Add"){_, _ ->
                 //add button
                     val nameTextFiled=dialogView.findViewById<EditText>(R.id.addChannelNameText)
                     val descTextField=dialogView.findViewById<EditText>(R.id.addChannelDescText)
                     val channelName=nameTextFiled.text.toString()
                     val channelDesc=descTextField.text.toString()
                     //create channel with the channel name and description
                     socket.emit("newChannel",channelName,channelDesc)



                 }
                 .setNegativeButton("Cancel"){_, _ ->
                  //cencel butn

                 }
                 .show()


        }

    }
    //send message
    fun sendMsgBtnClicked(view: View){
        if (App.prefs.isLoggedIn && messageTextField.text.isNotEmpty() && selectedChannel != null) {
            val userId = UserDataService.id
            val channelId = selectedChannel!!.id
            socket.emit("newMessage", messageTextField.text.toString(), userId, channelId,
                UserDataService.name, UserDataService.avatarName, UserDataService.avatarColor)
            messageTextField.text.clear()
            hidekeyboard()
        }












    }
    private  val onNewChannel=Emitter.Listener { args ->
        if(App.prefs.isLoggedIn){
            runOnUiThread {
                val channelName=args[0] as String
                val channelDescription=args[1] as String
                val channelId=args[2] as String

                val newChannel=Channel(channelName,channelDescription,channelId)
                MessageService.channels.add(newChannel)
                channelAdapter.notifyDataSetChanged()
            }

        }


    }
    private  val  onNewMessage=Emitter.Listener { args ->
       if(App.prefs.isLoggedIn){
           runOnUiThread {
               val channelId=args[2] as String

               if (channelId==selectedChannel?.id){
                   val msgBody=args[0] as String

                   val userName=args[3] as String
                   val userAvatar=args[4] as String
                   val userAvatarColor=args[5] as String
                   val id=args[6] as String
                   val timeStamp=args[7] as String

                   val newMessage=Message(msgBody,userName,channelId,userAvatar,userAvatarColor,id,timeStamp)
                   MessageService.messages.add(newMessage)
                   messageAdapter.notifyDataSetChanged()
                   messageListView.smoothScrollToPosition(messageAdapter.itemCount-1)

               }



           }
       }
    }


    fun hidekeyboard(){
        val inputManager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }
    }
}
