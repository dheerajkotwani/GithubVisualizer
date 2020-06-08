/*
 * MIT License
 *
 * Copyright (c) 2020 Dheeraj Kotwani
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package project.dheeraj.githubvisualizer.Activity

import GithubUserModel
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_developer_info.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_repository_info.*
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.content_profile.llEmail
import kotlinx.android.synthetic.main.content_profile.llLocation
import kotlinx.android.synthetic.main.content_profile.llOrganisations
import kotlinx.android.synthetic.main.content_profile.llTwitter
import kotlinx.android.synthetic.main.content_profile.llWebsite
import kotlinx.android.synthetic.main.content_profile.tvDisplayName
import kotlinx.android.synthetic.main.fragment_profile.*
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.GithubApiClient
import project.dheeraj.githubvisualizer.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class DeveloperInfoActivity : AppCompatActivity() {

    private lateinit var sharedPref:SharedPreferences
    private lateinit var owner: String
    private lateinit var repo: String
    private var star = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_info)

        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);
        getUserData(apiInterface, "dheerajkotwani")

        Glide.with(this@DeveloperInfoActivity)
            .load(R.drawable.dheeraj_profile)
            .into(developerImage)

        checkFollow(apiInterface, "dheerajkotwani")
        followDheeraj.setOnClickListener {
                followUser(apiInterface, "dheerajkotwani")
        }

        llGithubDheeraj.setOnClickListener{
            var intent = Intent(this,
                ProfileActivity::class.java)
            intent.putExtra("login", "dheerajkotwani")
//            intent.putExtra("avatar", searchModel[position].avatar_url)
            startActivity(intent)
        }

        owner = "dheerajkotwani"
        repo = "GithubVisualizer"

        getStar(apiInterface)

        starGithubVisualiser.setOnClickListener {
            putStar(apiInterface)
        }

    }

    private fun followUser(apiInterface: GithubApiInterface,
                           username: String){

        var call =
            apiInterface.followUser("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", username)

        call.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                followDheeraj.isClickable = true
                Toast.makeText(this@DeveloperInfoActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                followDheeraj.isClickable = true
                if (response.code() == 204){
                    Toast.makeText(this@DeveloperInfoActivity, "User Followed", Toast.LENGTH_SHORT).show()
                    followDheeraj.text = "Following"
                }
                else {
                    Toast.makeText(this@DeveloperInfoActivity, "Following", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun checkFollow(apiInterface: GithubApiInterface,
                            username: String){

        var call =
            apiInterface.checkFollow("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", username)

        call.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                Timber.e(t)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == 404){
                    followDheeraj.text = "Follow"
                }
                else {
                    followDheeraj.text = "Following"
                }
            }

        })
    }

    private fun getUserData(
        apiInterface: GithubApiInterface,
        username: String
    ) {
        var call: Call<GithubUserModel> =
            apiInterface.getPublicUser("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", "dheerajkotwani")
        try {
            call.enqueue(object : Callback<GithubUserModel> {
                override fun onFailure(call: Call<GithubUserModel>, t: Throwable) {
                    Toast.makeText(
                        baseContext,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<GithubUserModel>,
                    response: Response<GithubUserModel>
                ) {

                    try {
                        tvFollowersDheeraj.text = response.body()!!.followers.toString()
                        tvFollowingDheeraj.text = response.body()!!.following.toString()
                        tvRepositoriesDheeraj.text =
                            (response.body()!!.public_repos).toString()

                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun getStar(
        apiInterface: GithubApiInterface
    ) {
        var call: Call<String> =
            apiInterface.getStar("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", owner, repo)
        try {
            call.enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(
                        baseContext,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {

                    try {

                        if (response.code() == 204){
                            star = true
                            starGithubVisualiser.text = "Starred"
                        }
                        else {
                            star = false
                            starGithubVisualiser.text = "Star"
                        }

                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun putStar(
        apiInterface: GithubApiInterface
    ) {
        var call: Call<String> =
            apiInterface.starTheRepo("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", owner, repo)
        try {
            call.enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(
                        baseContext,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {

                    try {

                        if (response.code() == 204){
                            star = true
                            starGithubVisualiser.text = "Starred"
                            Toast.makeText(this@DeveloperInfoActivity, "Starred", Toast.LENGTH_SHORT).show()

                        }
                        else {
                            star = false
                            Toast.makeText(this@DeveloperInfoActivity, "Already Starred", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
