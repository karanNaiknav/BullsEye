

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import com.example.groupproject.BullsEye
import com.example.groupproject.R

class BullsEyeView : View {
    val MA : String = "com.example.groupproject.MainActivity"
    private var paint : Paint = Paint( )
    private var height : Int  = 0
    private var width: Int = 0

    private lateinit var targetBoard : Bitmap
    private lateinit var targetRect : Rect

    private lateinit var glassPaneOne: Rect
    private lateinit var glassPaneTwo: Rect

    private var game : BullsEye

    constructor (context : Context, width : Int, height : Int  ) : super( context ) {
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.strokeWidth = 20.0f

        this.height = height
        this.width = width

        // loads in the target
        var options = BitmapFactory.Options()
        options.inScaled = false
        var originalBoard = BitmapFactory.decodeResource( resources, TARGET, options )
        targetBoard = Bitmap.createScaledBitmap(originalBoard,originalBoard.width/2, originalBoard.height/2, false)

        // scale factor to make the image smaller
        val scale_factor: Float = 10f
        var scale : Float = ( width / scale_factor ) / targetBoard.width
        var newTargetHeight : Float  = targetBoard.height * scale
        targetRect = Rect( width - (width/scale_factor.toInt()), 0,width, newTargetHeight.toInt() )

        // we can change game speed and stuff here
        game = BullsEye( context, targetRect, 5, .03f, .2f,width, height)
        game.setTargetSpeed( width * .00003f )
        game.setBulletSpeed( width * .00035f )
        game.setHuntingRect( Rect( 0, 0, width, height ) )
        game.setDeltaTime( DELTA_TIME )
        game.setCannon( Point( 0, height ),width / 30, width/15, width / 50 )
    }

    fun getGame( ) : BullsEye {
        return game
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // draw the View here

        // draw the cannon base
        var cx : Float = game.getCannonCenter().x.toFloat()
        var cy : Float = game.getCannonCenter().y.toFloat()
        canvas.drawCircle( cx, cy, game.getCannonRadius().toFloat(), paint )

        // draw cannon barrel
        var bx : Float = game.getBarrelLength() * Math.cos( game.getCannonAngle().toDouble() ).toFloat()
        var by : Float = game.getBarrelLength() * Math.sin( game.getCannonAngle().toDouble() ).toFloat()
        canvas.drawLine( cx, cy, cx + bx, cy - by, paint )

        // V2 - draw the bullet
        var bulletX : Float = game.getBulletCenter().x.toFloat()
        var bulletY : Float = game.getBulletCenter().y.toFloat()
        canvas.drawCircle( bulletX, bulletY, game.getBulletRadius().toFloat(), paint )

        // draw the duck
        if( game.isTargetShot() )
//            canvas.drawBitmap(ducks[0], null, game.getDuckRect(), paint )
        else {
//            canvas.drawBitmap(ducks[duckFrame], null, game.getDuckRect(), paint)
//            duckFrame = (duckFrame + 1) % ducks.size
            canvas.drawBitmap(targetBoard,null, game.getTargetRect(), paint)
        }

        // draw the glass panes
        var oneRight = game.getPaneOne().right.toFloat()
        var oneTop = game.getPaneOne().top.toFloat()
        var oneLeft = game.getPaneOne().left.toFloat()
        var oneBottom = game.getPaneOne().bottom.toFloat()
        canvas.drawRect(oneLeft,oneTop,oneRight,oneBottom,paint)

        var twoRight = game.getPaneTwo().right.toFloat()
        var twoTop = game.getPaneTwo().top.toFloat()
        var twoLeft = game.getPaneTwo().left.toFloat()
        var twoBottom = game.getPaneTwo().bottom.toFloat()
        canvas.drawRect(twoLeft,twoTop,twoRight,twoBottom,paint)
    }

    companion object {
        val TARGET : Int = R.drawable.target

        val DELTA_TIME : Int = 100
    }
}