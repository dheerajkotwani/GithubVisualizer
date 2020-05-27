package project.dheeraj.githubvisualizer.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import project.dheeraj.githubvisualizer.R

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var icon:ImageView
    lateinit var usernameEditText:TextInputEditText
    lateinit var passwordEditText:TextInputEditText
    lateinit var loginButton:Button
    lateinit var username:String
    lateinit var password:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initialiseViews()

        loginButton.setOnClickListener(this)



    }

    private fun initialiseViews() {
        icon = findViewById(R.id.github_icon)
        usernameEditText = findViewById(R.id.et_username)
        passwordEditText = findViewById(R.id.et_password)
        loginButton = findViewById(R.id.button_login)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.button_login->checkFields()

        }
    }

    private fun checkFields() {
        username = usernameEditText.text.toString()
        password = passwordEditText.text.toString()

        if (username.isNullOrBlank()){
            usernameEditText.error = "Username can't be empty"
            usernameEditText.requestFocus()
            return
        }
        if (password.isNullOrBlank()){
            passwordEditText.error = "Password can't be empty"
            passwordEditText.requestFocus()
            return
        }

        verifyCredentials(username, password)

    }

    private fun verifyCredentials(username: String, password: String) {
        Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
    }
}
