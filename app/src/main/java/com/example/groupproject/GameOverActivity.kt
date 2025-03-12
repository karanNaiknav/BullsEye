package com.example.groupproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class GameOverActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)

        // Get FireBase databse reference and get the email of the player.
        // Update the value of the player's top score.


        Log.w(MA, "INSIDE ONCREATE GAMEOVER")
        var reference: DatabaseReference = MainActivity.firebase.getReference("USERS")
        var childref = reference.child(savedName)


        Log.w(MA, savedName)

        //if(savedName != "")
        childref.setValue(BullsEyeActivity.game.getBestScore())


        // set up enet handling
        var listener = DataListener( )
        reference.addValueEventListener( listener )

        val reviewAppButton = findViewById<Button>(R.id.review_app)
        reviewAppButton.setOnClickListener {
            reviewApp()
        }
        val playAgainButton = findViewById<Button>(R.id.play_again)
        playAgainButton.setOnClickListener {
            playAgain()
        }

        updateScore()

    }
    fun updateScore(){
        var topScoreTV: TextView = findViewById<Button>(R.id.top_score)
        var currScoreTV: TextView = findViewById<Button>(R.id.current_score)

        topScoreTV.text = "Top Score: " + BullsEyeActivity.game.getBestScore().toString()
        currScoreTV.text = "Round Score: " + BullsEyeActivity.game.getRoundScore().toString()

    }

    fun playAgain(){
        Log.w(MA, "PLAY AGAIN")
        BullsEyeActivity.game.restartGame()
        //setContentView(BullsEyeActivity.gameView)

        val myActivity: Intent = Intent(this, BullsEyeActivity::class.java)
        this.startActivity(myActivity)

    }

    fun reviewApp(){
        val myActivity: Intent = Intent(this, RateAppScreen::class.java)
        this.startActivity(myActivity)
    }

    inner class DataListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            Log.w(MA,"ONDATA CHANGE CALLED")
            //var users:Array<String> = arrayOf()
            var users = mutableListOf<String>()

            //var users: Array<String> = arrayOf("Ray\t2","Joe\t3","Bill\t5")

            if (snapshot.hasChildren())
                Log.w(MA, "Snapshot has children")
            else{
                Log.w(MA, "Snapshot NO children")
            }


            // Loop through each child node in the snapshot
            for (st in snapshot.children) {
                val key = st.key
                val data = st.value

                Log.w(MA, "st.key: " + key)
                Log.w(MA, "st.value: " + data)

                // Add the data to the list
                if (data != null) {
                    //users += key + "\t" + data
                    users.add(key + "\t" + data)
                }
            }
            val adapter: ArrayAdapter<*> = ArrayAdapter(
                this@GameOverActivity, android.R.layout.simple_list_item_1,users
            )

            val listView = findViewById<ListView>(R.id.leaderboard)
            listView.adapter = adapter

            Log.w(MA,"USERS ARRAY: " + users.size)

        }


        override fun onCancelled(error: DatabaseError) {
            Log.w(MA, "reading failure: " + error.message)
        }


    }

    companion object {
        const val MA: String = "MainActivity"
        var savedName: String = MainActivity.name
    }
}