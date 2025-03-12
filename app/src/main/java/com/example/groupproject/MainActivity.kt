package com.example.groupproject
import BullsEyeView
import GameTimerTask
import android.annotation.SuppressLint
import android.content.Intent
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Timer
/*
Final Group Project Made by:
Karan Naiknavare, Tarun Narahari, Raymond Yu
Bulls Eye App
 */
class MainActivity : AppCompatActivity() {
    val MA : String = "com.example.groupproject.MainActivity"
    private lateinit var username: EditText
    private lateinit var useremail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.w(GameOverActivity.MA, "INSIDE ONCREATE MAINNN")

        setContentView(R.layout.activity_main)

        var statusBarHeightId : Int =
            resources.getIdentifier( "status_bar_height", "dimen", "android" )
        var statusBarHeight : Int = resources.getDimensionPixelSize( statusBarHeightId )

        val startGameButton = findViewById<Button>(R.id.SubmitInfo)
        startGameButton.setOnClickListener {
            gamePage()
        }


    }

    fun gamePage(){
        Log.w(GameOverActivity.MA, "Inside GamePage function")
        username = findViewById(R.id.userName)
        useremail = findViewById(R.id.userEmail)

        name = username.text.toString()
        email = useremail.text.toString()

        Log.w(GameOverActivity.MA, name + "  " + email)


        var reference: DatabaseReference = firebase.getReference("USERS")


        var childref: DatabaseReference = reference.child(name)

        // set up event handling
        var listener = DataListener()
        reference.addValueEventListener(listener)
        childref.addValueEventListener( listener )

        childref.setValue("0")


        val myActivty:Intent = Intent(this,BullsEyeActivity::class.java)
        this.startActivity(myActivty)
    }

    inner class DataListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.hasChildren())
                Log.w(GameOverActivity.MA, "FIRST Snapshot has children")
            else{
                Log.w(GameOverActivity.MA, "FIRST Snapshot NO children")
            }

            var key: String? = snapshot.key
            Log.w(MA, "key is " + key)
            var valueObject: Any? = snapshot.value
            if (valueObject != null) {
                var value: String = valueObject.toString()
                Log.w(MA, "value is " + value)
            } else {
                Log.w(MA, "No value found")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(MA, "reading failure: " + error.message)
        }

    }

    companion object {

        var name: String = ""
        var email: String = ""
        var firebase: FirebaseDatabase = FirebaseDatabase.getInstance()
    }
}

