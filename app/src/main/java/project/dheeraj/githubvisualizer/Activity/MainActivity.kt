package project.dheeraj.githubvisualizer.Activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import project.dheeraj.githubvisualizer.*
import project.dheeraj.githubvisualizer.AppConfig.ACCESS_TOKEN
import project.dheeraj.githubvisualizer.AppConfig.SHARED_PREF
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    private lateinit var apiInterface: GithubApiInterface
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        Toast.makeText(this, sharedPref.getString(ACCESS_TOKEN, null), Toast.LENGTH_LONG).show()


        apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);
        var call: Call<ProfileModel> =
            apiInterface.getUserInfo("token ${sharedPref.getString(ACCESS_TOKEN, "")}")
        call.enqueue(object : Callback<ProfileModel> {
            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<ProfileModel>, response: Response<ProfileModel>) {
                Toast.makeText(
                    this@MainActivity,
                    "response: ${response.body()!!.bio}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
