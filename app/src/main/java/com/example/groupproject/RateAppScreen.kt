package com.example.groupproject
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.content.Intent
import android.net.Uri
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener


class RateAppScreen : AppCompatActivity() {
    private lateinit var input : EditText
    private lateinit var ratingBar : RatingBar
    private lateinit var submit : Button
    private lateinit var adView : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rate_app)
        input = findViewById(R.id.Input)
        ratingBar = findViewById(R.id.ratingBar)
        submit = findViewById(R.id.submit)
        submit.setOnClickListener {
            sendEmail()
        }

        var initializer: AdInitializer = AdInitializer()
        MobileAds.initialize(this,initializer)
        adView = AdView( this )
        var adSize : AdSize = AdSize( AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT )
        adView.setAdSize( adSize )
        var adUnitId : String = "ca-app-pub-3940256099942544/6300978111"
        adView.adUnitId = adUnitId
        // create an AdRequest
        var builder : AdRequest.Builder = AdRequest.Builder( )
        builder.addKeyword( "workout" ).addKeyword( "fitness" )
        var request : AdRequest = builder.build()
        // put the AdView in the LinearLayout
        var adLayout : LinearLayout = findViewById( R.id.ad_view )
        adLayout.addView( adView )
        // load the ad
        adView.loadAd( request )


    }

    class AdInitializer : OnInitializationCompleteListener {
        override fun onInitializationComplete(p0: InitializationStatus) {
            Log.w("MainActivity", "hi")
        }
    }

    private fun sendEmail() {
        val emailAddress: String = input.text.toString()
        val rating: String = ratingBar.rating.toString()
        Log.w("MainActivity", rating + "," + emailAddress)

        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, "Bullseye Rating")
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, rating)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }




    }


}