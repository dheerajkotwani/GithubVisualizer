package project.dheeraj.githubvisualizer.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.os.postDelayed
import com.google.firebase.auth.FirebaseAuth
import project.dheeraj.githubvisualizer.R

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        firebaseAuth =  FirebaseAuth.getInstance()

        val SPLASH_TIME_OUT = 3000

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            if (firebaseAuth.currentUser!=null) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            else{
                startActivity(Intent(this,LoginActivity::class.java))
            }
            // close this activity
            finish()
        }, 3000)

    }
}
