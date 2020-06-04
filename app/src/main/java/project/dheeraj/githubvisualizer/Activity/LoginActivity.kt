package project.dheeraj.githubvisualizer.Activity

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
        if (auth.currentUser!=null)
            startActivity(Intent(this, MainActivity::class.java))
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

//    private fun getLoginService(token: String?): LoginService? {
//        return AppRetrofit()
//            .getRetrofit(AppConfig.GITHUB_API_BASE_URL, token.toString())!!
//            .create(LoginService::class.java)
//    }

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

//       var intent: Intent = Intent(Intent.ACTION_VIEW,
//            Uri.parse("https://github.com/login/oauth/authorize?client_id=${AppConfig.CLIENT_ID}" +
//                    "&scope=${AppConfig.OAUTH2_SCOPE}" +
//                    "&login=${username}" +
//                    "&redirect_uri=${AppConfig.REDIRECT_URL}"));
//        startActivity(intent)


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

//                Toast.makeText(this," access Token:  ${(it!!.credential as OAuthCredential).accessToken}", Toast.LENGTH_LONG).show()
//                Toast.makeText(this,"idToken:  ${(it!!.credential as OAuthCredential).idToken}", Toast.LENGTH_LONG).show()
//                Toast.makeText(this," secret:  ${(it!!.credential as OAuthCredential).secret}", Toast.LENGTH_LONG).show()

//                usernameEditText.setText(((it as zzh) as zzc).toString());

//                var json:String = it.user!!.zzc().persistenceKey;
//
//                    var obj: AuthResult? = it;
//                    (obj!!.credential as OAuthCredential).accessToken

                with (sharedPref.edit()) {
                    putString(ACCESS_TOKEN, (it!!.credential as OAuthCredential).accessToken)
                    commit()
                }

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))



                val apiInterface =
                        GithubApiClient.getClient().create(GithubApiInterface::class.java);
                    var call: Call<ProfileModel> =
                        apiInterface.getUserInfo("token ${(it!!.credential as OAuthCredential).accessToken}")
                    call.enqueue(object : Callback<ProfileModel> {
                        override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                            Toast.makeText(
                                this@LoginActivity,
                                "error: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(call: Call<ProfileModel>, response: Response<ProfileModel>) {
                            Log.d("RESPONSE", response.message())
                            Log.d("UserName", response.body()!!.name)

                            Toast.makeText(
                                this@LoginActivity,
                                "response: ${response.body()!!.bio}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })

            }

    }

}

