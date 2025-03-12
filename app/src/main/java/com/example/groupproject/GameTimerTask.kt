

import android.util.Log
import com.example.groupproject.BullsEyeActivity
import com.example.groupproject.MainActivity
import java.util.TimerTask

class GameTimerTask : TimerTask {
    private lateinit var activity : BullsEyeActivity

    constructor ( activity : BullsEyeActivity) {
        this.activity = activity
    }

    override fun run() {
        // update the model
        activity.updateModel( )
        // update the view
        activity.updateView()
        if (activity.getGameMain().panesHit()){
            Log.w("MA", "Inside if statment")
            cancel()
            activity.toFinalScreen()
        }
    }
}