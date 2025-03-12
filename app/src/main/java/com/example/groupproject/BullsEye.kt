package com.example.groupproject
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import java.util.*
import kotlin.math.round

class BullsEye {
    private var huntingRect: Rect? = null
    private var deltaTime = 0 // in milliSeconds
    private var targetRect: Rect? = null
    private var targetWidth = 0
    private var targetHeight = 0
    private var targetSpeed = 0f
    private var targetShot: Boolean  = false
    private var cannonCenter: Point? = null
    private var cannonRadius = 0
    private var barrelLength = 0
    private var barrelRadius = 0
    private var cannonAngle: Float = 0.0f
    private var bulletCenter: Point? = null
    private var bulletRadius = 0
    private var bulletFired: Boolean = false
    private var bulletAngle = 0f
    private var bulletSpeed = 0f
    private var random: Random? = null
    private var glassPane1: Rect? = null
    private var glassPane2: Rect? = null
    private var height = 0
    private var width = 0
    private var speed1 = 10
    private var speed2 = -10
    private var roundScore = 0


    constructor(context: Context, newTargetRect: Rect?, newBulletRadius: Int,
                newTargetSpeed: Float, newBulletSpeed: Float, width:Int, height:Int ) {
        setTargetRect(newTargetRect)
        setTargetSpeed(newTargetSpeed)
        setBulletRadius(newBulletRadius)
        setBulletSpeed(newBulletSpeed)
        setGlassPane1and2()
        random = Random()
        bulletFired = false
        targetShot = false
        cannonAngle = Math.PI.toFloat() / 4 // starting cannon angle
        this.height = height
        this.width = width
        var pref : SharedPreferences =
            context.getSharedPreferences( context.packageName + "_preferences",
                Context.MODE_PRIVATE )
        setLevel( pref.getInt( LEVEL, 0))
    }
    fun setLevel(level:Int){
        bestScore = level
    }
    fun setPreferences( context : Context ) {
        var pref : SharedPreferences =
            context.getSharedPreferences( context.packageName + "_preferences",
                Context.MODE_PRIVATE )
        var edit : SharedPreferences.Editor = pref.edit()

        edit.putInt( LEVEL , bestScore )

        edit.commit()
        Log.w("BULLS EYE", bestScore.toString())

    }

    fun getBestScore(): Int{
        return bestScore
    }

    fun getRoundScore():Int{
        return roundScore
    }

    fun getCannonCenter(): Point {
        return cannonCenter!!
    }

    fun getPaneOne(): Rect {
        return glassPane1!!
    }

    fun getPaneTwo(): Rect {
        return glassPane2!!
    }

    fun getCannonRadius(): Int {
        return cannonRadius
    }

    fun getBarrelLength(): Int {
        return barrelLength
    }

    fun getBulletCenter(): Point {
        return bulletCenter!!
    }

    fun getTargetRect(): Rect {
        return targetRect!!
    }

    fun getHuntingRect(): Rect? {
        return huntingRect
    }

    fun setHuntingRect(newHuntingRect: Rect?) {
        if (newHuntingRect != null)
            huntingRect = newHuntingRect
    }

    fun setGlassPane1and2(){
        glassPane1 = Rect(100, 300, 200, 400)
        glassPane2 = Rect(2000, 500, 2100, 600)
    }

    fun changeGlassPaneLengthByFactor(factor:Int){
        glassPane1!!.left -=  factor
        glassPane1!!.right += factor

        glassPane2!!.left -=  factor
        glassPane2!!.right += factor

    }

    fun setDeltaTime(newDeltaTime: Int) {
        if (newDeltaTime > 0)
            deltaTime = newDeltaTime
    }

    fun setTargetRect(newTargetRect: Rect?) {
        if (newTargetRect != null) {
            targetWidth = newTargetRect.right - newTargetRect.left
            targetHeight = newTargetRect.bottom - newTargetRect.top
            targetRect = newTargetRect
        }
    }

    fun setTargetSpeed(newTargetSpeed: Float) {
        if (newTargetSpeed > 0) targetSpeed = newTargetSpeed
    }

    fun setCannon(
        newCannonCenter: Point?, newCannonRadius: Int,
        newBarrelLength: Int, newBarrelRadius: Int
    ) {
        if (newCannonCenter != null && ( newCannonRadius > 0 ) && ( newBarrelLength > 0 ) ) {
            cannonCenter = newCannonCenter
            cannonRadius = newCannonRadius
            barrelLength = newBarrelLength
            barrelRadius = newBarrelRadius
            bulletCenter = Point(
                (cannonCenter!!.x + cannonRadius * Math.cos(cannonAngle.toDouble())).toInt(),
                (cannonCenter!!.y - cannonRadius * Math.sin(cannonAngle.toDouble())).toInt()
            )
        }
    }

    fun getBulletRadius(): Int {
        return bulletRadius
    }

    fun setSpeed(){
        if (speed1 < 0 ){
            speed1 -= 10
        }
        if (speed2 < 0 ){
            speed2 -= 10
        }
        if (speed2 > 0 ){
            speed2 += 10
        }
        if (speed1 > 0 ){
            speed1 += 10
        }

    }
    fun setBulletRadius(newBulletRadius: Int) {
        if (newBulletRadius > 0)
            bulletRadius = newBulletRadius
    }

    fun setBulletSpeed(newBulletSpeed: Float) {
        if (newBulletSpeed > 0)
            bulletSpeed = newBulletSpeed
    }

    fun getCannonAngle(): Float {
        return cannonAngle
    }

    fun setCannonAngle(newCannonAngle: Float) {
        if (newCannonAngle >= 0 && newCannonAngle <= Math.PI / 2)
            cannonAngle = newCannonAngle
        else if (newCannonAngle < 0)
            cannonAngle = 0f
        else
            cannonAngle = Math.PI.toFloat() / 2
        if (!bulletFired)
            loadBullet()
    }

    fun fireBullet() {
        bulletFired = true
        bulletAngle = cannonAngle
    }

    fun isBulletFired(): Boolean {
        return bulletFired
    }

    fun isTargetShot(): Boolean {
        return targetShot
    }

    fun setTargetShot(newTargetShot: Boolean) {
        targetShot = newTargetShot
    }

    fun startTargetFromRightTopHalf() {
        targetRect!!.left = huntingRect!!.right
        targetRect!!.right = targetRect!!.left + targetWidth
        targetRect!!.top = 0
        targetRect!!.bottom = targetRect!!.top + targetHeight
    }

    fun moveTarget(context: Context) {
        if (!targetShot) { // move left
            targetRect!!.left -= (targetSpeed * deltaTime).toInt()
            targetRect!!.right -= (targetSpeed * deltaTime).toInt()
        } else { // move down
            targetRect!!.top += (5 * targetSpeed * deltaTime).toInt()
            targetRect!!.bottom += (5 * targetSpeed * deltaTime).toInt()
        }
    }


    fun nextRound(context: Context){
        roundScore+=1
        // display a screen here, and then move to the next round
        Log.w("Round Score: ", ""+ roundScore)
        if(roundScore>bestScore){
            setLevel(roundScore)
            setPreferences(context)
        }
        setSpeed()
        changeGlassPaneLengthByFactor(30)
        setTargetSpeed(targetSpeed + .08f)

    }

    fun moveGlassPane1(){
        bounceOffWalls()
        glassPane1!!.left = glassPane1!!.left + speed1
        glassPane1!!.right = glassPane1!!.right + speed1
    }

    fun moveGlassPane2(){
        bounceOffWalls()
        glassPane2!!.left = glassPane2!!.left + speed2
        glassPane2!!.right = glassPane2!!.right + speed2
    }

    private fun bounceOffWalls(){
        if (glassPane1!!.left <= 0 ){
            var diff = glassPane1!!.left
            glassPane1!!.left = 1
            glassPane1!!.right += -1 *(diff+1)
            speed1 = speed1 * -1
        }
        if (glassPane2!!.left <= 0 ){
            var diff = glassPane2!!.left
            glassPane2!!.left = 1
            glassPane2!!.right += -1 * (diff+1)
            speed2 = speed2 * -1
        }
        if (glassPane1!!.right >= width){
            var diff = glassPane1!!.right - width
            glassPane1!!.left -= diff+1
            glassPane1!!.right = width - 1
            speed1 = -speed1
        }
        if (glassPane2!!.right >= width){
            var diff = glassPane2!!.right - width
            glassPane2!!.left -= diff+1
            glassPane2!!.right = width - 1
            speed2 = -speed2
        }
    }


    fun targetOffScreen(): Boolean {
        return ( targetRect!!.right < 0 ) || ( targetRect!!.bottom < 0 )
                || ( targetRect!!.top > huntingRect!!.bottom )
                || ( targetRect!!.left > huntingRect!!.right )
    }

    fun moveBullet() {
        bulletCenter!!.x += (bulletSpeed * Math.cos(bulletAngle.toDouble()) * deltaTime).toInt()
        bulletCenter!!.y -= (bulletSpeed * Math.sin(bulletAngle.toDouble()) * deltaTime).toInt()
    }


    fun bulletOffScreen(): Boolean {
        return (bulletCenter!!.x - bulletRadius > huntingRect!!.right
                || bulletCenter!!.y + bulletRadius < 0)
    }

    fun loadBullet() {
        bulletFired = false
        bulletCenter!!.x = (cannonCenter!!.x
                + cannonRadius * Math.cos(cannonAngle.toDouble())).toInt()
        bulletCenter!!.y = (cannonCenter!!.y
                - cannonRadius * Math.sin(cannonAngle.toDouble())).toInt()
    }

    fun targetHit(): Boolean {
        return targetRect!!.intersects(
            bulletCenter!!.x - bulletRadius, bulletCenter!!.y - bulletRadius,
            bulletCenter!!.x + bulletRadius, bulletCenter!!.y + bulletRadius
        )
    }

    fun deleteBullet(){
        bulletCenter = null
    }

    fun panesHit(): Boolean {

        if(isBulletFired()) {
            Log.w(
                "test",
                "glass panel ${glassPane1!!.left}, ${glassPane1!!.right}, ${glassPane1!!.top}, ${glassPane1!!.bottom}"
            )
            Log.w(
                "test",
                "bullet ${bulletCenter!!.x - bulletRadius}, ${bulletCenter!!.x + bulletRadius}, ${bulletCenter!!.y - bulletRadius}, ${bulletCenter!!.y + bulletRadius}"
            )

            return glassPane1!!.intersects(
                bulletCenter!!.x - bulletRadius, bulletCenter!!.y - bulletRadius,
                bulletCenter!!.x + bulletRadius, bulletCenter!!.y + bulletRadius
            ) or glassPane2!!.intersects(
                bulletCenter!!.x - bulletRadius, bulletCenter!!.y - bulletRadius,
                bulletCenter!!.x + bulletRadius, bulletCenter!!.y + bulletRadius
            )
        }
        return false
    }

    fun restartGame(){
        roundScore = 0
        speed1 = 10
        speed2 = -10
        setTargetSpeed( width * .00003f )
        setGlassPane1and2()
    }
    companion object {
        private const val LEVEL : String = "level"
        private var bestScore: Int = 0

    }
}


