package com.example.groupproject

import BullsEyeView
import GameTimerTask
import android.annotation.SuppressLint
import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer


class BullsEyeActivity :  AppCompatActivity() {

    //private lateinit var gameView : BullsEyeView
    private lateinit var detector : GestureDetector
    val MA : String = "com.example.groupproject.MainActivity"

    private lateinit var pool : SoundPool
    private var soundFireId = 0
    private var targetHitId = 0
    private var roundHit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var width : Int  = resources.displayMetrics.widthPixels
        var height : Int = resources.displayMetrics.heightPixels

        var statusBarHeightId : Int =
            resources.getIdentifier( "status_bar_height", "dimen", "android" )
        var statusBarHeight : Int = resources.getDimensionPixelSize( statusBarHeightId )

        gameView = BullsEyeView(this, width, height - statusBarHeight)
        game = gameView.getGame( )


        Log.w("DB CHECK", "GAME SCORE:"+ game.getBestScore())

        // event handling
        var th : TouchHandler = TouchHandler()
        detector = GestureDetector( this, th )
        detector.setOnDoubleTapListener( th )


        var gameTimer : Timer = Timer( )
        var task : GameTimerTask = GameTimerTask(this)
        gameTimer.schedule( task, 0L, 50L)

        // V3 - sound
        var builder : SoundPool.Builder = SoundPool.Builder( )
        pool = builder.build()
        soundFireId = pool.load( this, R.raw.cannon_fire, 1 )
        targetHitId = pool.load( this, R.raw.duck_hit, 1 )

        setContentView(gameView)
    }


    fun getGameMain() : BullsEye {
        return game
    }

    fun gamePage(v: View){
        setContentView(gameView)
    }

    fun toFinalScreen(){
        /* instead of finish, we want to go to the final gamepage */
        val myActivty: Intent = Intent(this,GameOverActivity::class.java)
        this.startActivity(myActivty)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if( event != null )
            detector.onTouchEvent( event )
        return super.onTouchEvent(event)
    }

    @SuppressLint("SuspiciousIndentation")
    fun updateModel( ) {
        // Log.w( MA, "Updating model" )
        game.moveTarget(this)
        game.moveGlassPane1()
        game.moveGlassPane2()
        if( game.targetOffScreen() ) {
            game.startTargetFromRightTopHalf()
            // V3
            game.setTargetShot( false )
        }

        // V3 - check if target is hit
        if( game.targetHit() ) {
            game.setTargetShot( true )
            playSound( targetHitId )
//            if(roundHit == false){
//                game.nextRound(this)
//                roundHit = true
//            }
            game.nextRound(this)

            Log.w("GAME ROUND SCORE:" ,""+game.getRoundScore())
            game.loadBullet()

        }


        // V2 - manage the bullet
        if( game.bulletOffScreen() )
            game.loadBullet()
        else if( game.isBulletFired() )
            game.moveBullet()
    }

    fun updateView( ) {
        // Log.w( MA, "Updating view" )
        gameView.postInvalidate()
    }

    // V2
    fun updateCannon( e : MotionEvent) {

        var x : Float = e.x - game.getCannonCenter().x
        var y : Float = game.getCannonCenter().y - e.y
        var angle : Double = Math.atan2( y.toDouble(), x.toDouble() )
        game.setCannonAngle( angle.toFloat() )
    }

    // V3 - play sound
    fun playSound( soundId : Int ) {
        pool.play( soundId, 1.0f, 1.0f, 0, 0, 1.0f )
    }

    // V2
    inner class TouchHandler : GestureDetector.SimpleOnGestureListener( ) {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            // update cannon angle
            Log.w( MA, "Inside onSingleTapConfirmed" )
            updateCannon( e )
            return super.onSingleTapConfirmed(e)
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            // update the cannon angle
            Log.w( MA, "Inside onScroll" )
            updateCannon( e2 )
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            // fire the bullet
            Log.w( MA, "Inside onDoubleTap" )

            if( ! game.isBulletFired() ) {
                game.fireBullet()
                playSound( soundFireId )
            }
            return super.onDoubleTap(e)
        }
    }

    companion object{
        lateinit var game : BullsEye
        lateinit var gameView : BullsEyeView

    }

}