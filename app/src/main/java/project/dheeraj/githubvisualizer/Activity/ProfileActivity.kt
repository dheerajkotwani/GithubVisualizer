package project.dheeraj.githubvisualizer.Activity

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.*
import project.dheeraj.githubvisualizer.R

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = intent.getStringExtra("login")

        Glide.with(baseContext)
            .load(intent.getStringExtra("avatar"))
            .into(userAvatar)

        Glide.with(baseContext)
            .load(intent.getStringExtra("avatar"))
            .into(userBackgroundImage)

    }
}
