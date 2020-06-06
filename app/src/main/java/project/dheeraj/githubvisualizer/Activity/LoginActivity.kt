package project.dheeraj.githubvisualizer.Activity

import GithubUserModel
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.common.internal.zzh
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.*
import okhttp3.*
import org.json.JSONObject
import project.dheeraj.githubvisualizer.*
import project.dheeraj.githubvisualizer.AppConfig.ACCESS_TOKEN
import project.dheeraj.githubvisualizer.AppConfig.LOGIN
import project.dheeraj.githubvisualizer.AppConfig.NAME
import project.dheeraj.githubvisualizer.AppConfig.SHARED_PREF
import project.dheeraj.githubvisualizer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var icon:ImageView
    private lateinit var usernameEditText:TextInputEditText
    private lateinit var passwordEditText:TextInputEditText
    private lateinit var loginButton:Button
    private lateinit var username:String
    private lateinit var password:String
    private lateinit var auth: FirebaseAuth
    private lateinit var githubAuthProvider: GithubAuthProvider
    private lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initialiseViews()
        if (auth.currentUser!=null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        loginButton.setOnClickListener(this)



    }

    private fun initialiseViews() {
        icon = findViewById(R.id.github_icon)
        usernameEditText = findViewById(R.id.et_username)
        passwordEditText = findViewById(R.id.et_password)
        loginButton = findViewById(R.id.button_login)
        auth = FirebaseAuth.getInstance()
        sharedPref = getSharedPreferences(
            SHARED_PREF, Context.MODE_PRIVATE)

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
//        if (password.isNullOrBlank()){
//            passwordEditText.error = "Password can't be empty"
//            passwordEditText.requestFocus()
//            return
//        }

        verifyCredentials(username, password)

    }

    private fun verifyCredentials(username: String, password: String) {

        var token: String = Credentials.basic(username, password);
        val provider: OAuthProvider.Builder = OAuthProvider.newBuilder("github.com")
        provider.addCustomParameter("login", username)
        val scopes: ArrayList<String?> = object : ArrayList<String?>() {
            init {
                add(AppConfig.OAUTH2_SCOPE)
            }
        }
        provider.scopes = scopes

        auth
            .startActivityForSignInWithProvider( /* activity= */this, provider.build())
            .addOnSuccessListener {
                // User is signed in.
                // IdP data available in
                // authResult.getAdditionalUserInfo().getProfile().
                // The OAuth access token can also be retrieved:
                // authResult.getCredential().getAccessToken().
                Toast.makeText(
                    this,
                    "Welcome ${auth.currentUser!!.displayName}",
                    Toast.LENGTH_SHORT
                ).show()


                with (sharedPref.edit()) {
                    putString(ACCESS_TOKEN, (it!!.credential as OAuthCredential).accessToken)
                    putString(NAME, auth.currentUser!!.displayName)
                    putString(NAME, auth.currentUser!!.email)
                    commit()
                }

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()


                val apiInterface =
                        GithubApiClient.getClient().create(GithubApiInterface::class.java);
                    var call: Call<GithubUserModel> =
                        apiInterface.getUserInfo("token ${(it!!.credential as OAuthCredential).accessToken}")
                    call.enqueue(object : Callback<GithubUserModel> {
                        override fun onFailure(call: Call<GithubUserModel>, t: Throwable) {
                            Toast.makeText(
                                this@LoginActivity,
                                "error: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(call: Call<GithubUserModel>, response: Response<GithubUserModel>) {
                            Log.d("RESPONSE", response.message())
                            Log.d("UserName", response.body()!!.login)

                            with (sharedPref.edit()) {
                                putString(ACCESS_TOKEN, (it!!.credential as OAuthCredential).accessToken)
                                putString(NAME, auth.currentUser!!.displayName)
                                putString(NAME, auth.currentUser!!.email)
                                commit()
                            }

//                            Toast.makeText(
//                                this@LoginActivity,
//                                "response: ${response.body()!!.bio}",
//                                Toast.LENGTH_LONG
//                            ).show()
                        }
                    })

            }

    }

}

