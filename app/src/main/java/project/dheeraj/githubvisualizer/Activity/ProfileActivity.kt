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
import RepositoryModel
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.toolbar
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.fragment_profile.llEmail
import kotlinx.android.synthetic.main.fragment_profile.llLocation
import kotlinx.android.synthetic.main.fragment_profile.llOrganisations
import kotlinx.android.synthetic.main.fragment_profile.llTwitter
import kotlinx.android.synthetic.main.fragment_profile.llWebsite
import kotlinx.android.synthetic.main.fragment_profile.tvDisplayName
import kotlinx.android.synthetic.main.fragment_profile.tvFollowers
import project.dheeraj.githubvisualizer.Adapter.RepositoryAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.Network.GithubApiClient
import project.dheeraj.githubvisualizer.Network.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var username: String
    private var followersCount: Int = 0
    private var starPage: Int = 1
    private var starRepo: ArrayList<RepositoryModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        username = intent.getStringExtra("login")
        supportActionBar!!.title = username
        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)

        // TODO implement clicks for different fields in desc eg. twitter, website

        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);

        fetchStars(apiInterface)
        getUserData(apiInterface, username)
        getTopRepos(username)

        if (sharedPref.getString(AppConfig.LOGIN, "") == username)
            buttonFollow.visibility = View.GONE
        else
            checkFollow(apiInterface, username)

        buttonFollow.setOnClickListener {
            buttonFollow.isClickable = false
            if (buttonFollow.text == "FOLLOWING")
                unfollowUser (apiInterface, username)
            else
                followUser (apiInterface, username)
        }


        // TODO
        llFollowersUser.setOnClickListener {
            //            DynamicToast.makeWarning(context!!, "Developing").show()
            val intent = Intent(this@ProfileActivity, FollowActivity::class.java)
            intent.putExtra(AppConfig.LOGIN, username)
            intent.putExtra("PAGE", "follower")
            startActivity(intent)
        }

        llFollowingUser.setOnClickListener {
            //            DynamicToast.makeWarning(context!!, "Developing").show()
            val intent = Intent(this@ProfileActivity, FollowActivity::class.java)
            intent.putExtra(AppConfig.LOGIN, username)
            intent.putExtra("PAGE", "following")
            startActivity(intent)
        }

        llRepoUser.setOnClickListener {
            //            DynamicToast.makeWarning(context!!, "Developing").show()
            val intent = Intent(this@ProfileActivity, RepositoriesActivity::class.java)
            intent.putExtra(AppConfig.LOGIN, username)
            intent.putExtra("USER_TYPE", "user")
            startActivity(intent)
        }

        // TODO
        llGistsUser.setOnClickListener {
//            DynamicToast.makeWarning(this@ProfileActivity, "Developing").show()
            val intent = Intent(this@ProfileActivity, RepositoriesActivity::class.java)
            intent.putExtra(AppConfig.LOGIN, username)
            intent.putExtra("USER_TYPE", "user")
            intent.putExtra("PAGE", "STARS")
            startActivity(intent)
        }


    }

    private fun followUser(apiInterface: GithubApiInterface,
                           username: String){

        var call =
            apiInterface.followUser("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", username)

        call.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                buttonFollow.isClickable = true
                Toast.makeText(this@ProfileActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                buttonFollow.isClickable = true
                if (response.code() == 204){
                    Toast.makeText(this@ProfileActivity, "User Followed", Toast.LENGTH_SHORT).show()
                    buttonFollow.text = "FOLLOWING"
                    followersCount++
                    tvFollowers.text = followersCount.toString()
                    Toast.makeText(this@ProfileActivity, "Followed", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this@ProfileActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    buttonFollow.isClickable = true

                }
            }

        })
    }

    private fun unfollowUser(apiInterface: GithubApiInterface,
                             username: String){

        var call =
            apiInterface.unfollowUser("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", username)

        call.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                buttonFollow.isClickable = true
                Toast.makeText(this@ProfileActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                buttonFollow.isClickable = true
                if (response.code() == 204){
                    Toast.makeText(this@ProfileActivity, "User Unfollowed", Toast.LENGTH_SHORT).show()
                    buttonFollow.text = "FOLLOW"
                    followersCount--
                    tvFollowers.text = followersCount.toString()
                    Toast.makeText(this@ProfileActivity, "Unfollowed", Toast.LENGTH_SHORT).show()
                }
                else {
                    buttonFollow.isClickable = true
                    Toast.makeText(this@ProfileActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
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
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (buttonFollow.visibility == View.GONE)
                    buttonFollow.visibility = View.VISIBLE

                if (response.code() == 404){
                    buttonFollow.text = "FOLLOW"


                }
                else if (response.code() == 204){
                    buttonFollow.text = "FOLLOWING"
                }
            }

        })
    }

    private fun getUserData(
        apiInterface: GithubApiInterface,
        username: String
    ) {
        var call: Call<GithubUserModel> =
            apiInterface.getPublicUser("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", username)
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

                        if (userInfoCard.visibility == View.GONE)
                            userInfoCard.visibility = View.VISIBLE


                        if (profileProgressBar.visibility == View.VISIBLE)
                            profileProgressBar.visibility = View.GONE

                        if (response.body()!!.name.isNullOrEmpty())
                            tvDisplayName.text = "Github User"
                        else
                            tvDisplayName.text = response.body()!!.name

                        if (response.body()!!.email.isNullOrEmpty())
                            llEmail.visibility = View.GONE
                        else
                            tvEmailUser.text = response.body()!!.email

                        if (response.body()!!.bio.isNullOrEmpty())
                            tvBioUser.visibility = View.GONE
                        else
                            tvBioUser.text = response.body()!!.bio

                        if (response.body()!!.company.isNullOrEmpty())
                            llOrganisations.visibility = View.GONE
                        else
                            tvOrganizationsUser.text = response.body()!!.company

                        if (response.body()!!.twitter_username.isNullOrEmpty())
                            llTwitter.visibility = View.GONE
                        else
                            tvTwitterUser.text = response.body()!!.twitter_username

                        if (response.body()!!.blog.isNullOrEmpty())
                            llWebsite.visibility = View.GONE
                        else
                            tvWebsitesUser.text = response.body()!!.blog

                        if (response.body()!!.location.isNullOrEmpty())
                            llLocation.visibility = View.GONE
                        else
                            tvLocationUser.text = response.body()!!.location

                        tvJoinedUser.text = "Joined: ${response.body()!!.created_at.subSequence(0, 10)}"
                        tvFollowersUser.text = response.body()!!.followers.toString()
                        followersCount = response.body()!!.followers
                        tvFollowingUser.text = response.body()!!.following.toString()
//                        tvGistsUser.text =
//                            "${response.body()!!.public_gists}"
                        tvRepositoriesUser.text =
                            (response.body()!!.public_repos).toString()

                        Glide.with(baseContext)
                            .load(response.body()!!.avatar_url)
                            .into(userAvatar)

                        Glide.with(baseContext)
                            .load(response.body()!!.avatar_url)
                            .into(userBackgroundImage)

                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun getTopRepos(
    username: String) {
        try {
            val apiInterface =
                GithubApiClient.getClient().create(GithubApiInterface::class.java);

            var call =
                apiInterface.topReposUser("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", username)
            call.enqueue(object : Callback<ArrayList<RepositoryModel>> {
                override fun onFailure(call: Call<ArrayList<RepositoryModel>>, t: Throwable) {
                    Timber.e(t)
                }

                override fun onResponse(
                    call: Call<ArrayList<RepositoryModel>>,
                    response: Response<ArrayList<RepositoryModel>>
                ) {
                    var repos: ArrayList<RepositoryModel> = ArrayList()


                    try {

                        repos = response.body()!!

                        topRepoRecyclerViewUser.adapter = RepositoryAdapter(this@ProfileActivity, repos)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }

                }

            })

        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun fetchStars(
        apiInterface: GithubApiInterface
    ) {

        var call: Call<ArrayList<RepositoryModel>> =
            apiInterface.starredRepoOfUser(
                "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}",
                username, starPage
            )
        try {


            call.enqueue(object : Callback<ArrayList<RepositoryModel>> {
                override fun onFailure(call: Call<ArrayList<RepositoryModel>>, t: Throwable) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<ArrayList<RepositoryModel>>,
                    response: Response<ArrayList<RepositoryModel>>
                ) {
                    try {

                        if (response.body()!!.size > 0) {
                            for (i in response.body()!!) {
                                starRepo.add(i)
                            }
                            starPage++
                            fetchStars(apiInterface)
                            tvGistsUser.text = "${starRepo.size}"
                        }
                        else
                            tvGistsUser.text = "${starRepo.size}"

                    } catch (e: Exception) {
                        Timber.e(e)
                        tvGistsUser.text = "${starRepo.size}"
                    }
                }
            })
        } catch (e: Exception) {
            Timber.e(e)
            tvGistsUser.text = "${starRepo.size}"
        }

    }

}
