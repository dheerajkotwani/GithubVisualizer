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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.toolbar
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.llEmail
import kotlinx.android.synthetic.main.fragment_profile.llLocation
import kotlinx.android.synthetic.main.fragment_profile.llOrganisations
import kotlinx.android.synthetic.main.fragment_profile.llTwitter
import kotlinx.android.synthetic.main.fragment_profile.llWebsite
import kotlinx.android.synthetic.main.fragment_profile.tvDisplayName
import project.dheeraj.githubvisualizer.Adapter.RepositoryAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.Network.GithubApiClient
import project.dheeraj.githubvisualizer.Network.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import project.dheeraj.githubvisualizer.ViewModel.ProfileViewModel
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
    private lateinit var token: String
    private lateinit var viewModel: ProfileViewModel
    private var follow = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        username = intent.getStringExtra("login")
        supportActionBar!!.title = username
        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        token = "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

//        tvFollowers = findViewById(R.id.tvFollowersUser)

        // TODO implement clicks for different fields in desc eg. twitter, website

        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);

        fetchStars(apiInterface)
        getUserData()
        getTopRepos()

        if (sharedPref.getString(AppConfig.LOGIN, "") == username) {
            buttonFollow.visibility = View.GONE
            viewModel.getLoginProfile(token)
            viewModel.getMyTopRepositories(token)
        }
        else {
            observeFollow()
            viewModel.getUserProfile(token, username)
            viewModel.getUserTopRepositories(token, username)
        }
        viewModel.getFollow(token, username)

        buttonFollow.setOnClickListener {
            buttonFollow.isClickable = false
            if (buttonFollow.text == "FOLLOWING")
                viewModel.deleteFollow(token, username)
            else
                viewModel.putFollow(token, username)
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

    private fun observeFollow(){

        viewModel.followData.observe(this, Observer {

            if (it == 404 && !buttonFollow.isVisible){
                buttonFollow.text = "FOLLOW"
                follow = false
                if (buttonFollow.visibility == View.GONE)
                    buttonFollow.visibility = View.VISIBLE
            }

            else if (it == 204 && !buttonFollow.isVisible){
                buttonFollow.text = "FOLLOWING"
                follow = true
                if (buttonFollow.visibility == View.GONE)
                    buttonFollow.visibility = View.VISIBLE
            }

            else if (it == 204 && buttonFollow.isVisible && follow) {
                follow = false
                Toast.makeText(this@ProfileActivity, "User Unfollowed", Toast.LENGTH_SHORT).show()
                buttonFollow.text = "FOLLOW"
                followersCount--
                tvFollowersUser.text = followersCount.toString()
            }

            else if (it == 204 && buttonFollow.isVisible && !follow) {
                follow = true
                Toast.makeText(this@ProfileActivity, "User Followed", Toast.LENGTH_SHORT).show()
                buttonFollow.text = "FOLLOWING"
                followersCount++
                tvFollowersUser.text = followersCount.toString()
            }

            else {
                Toast.makeText(this@ProfileActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            buttonFollow.isClickable = true

        })
    }

    private fun getUserData() {

        viewModel.userList.observe(this, Observer {
            if (userInfoCard.visibility == View.GONE)
                userInfoCard.visibility = View.VISIBLE


            if (profileProgressBar.visibility == View.VISIBLE)
                profileProgressBar.visibility = View.GONE

            if (it.name.isNullOrEmpty())
                tvDisplayName.text = "Github User"
            else
                tvDisplayName.text = it.name

            if (it.email.isNullOrEmpty())
                llEmail.visibility = View.GONE
            else
                tvEmailUser.text = it.email

            if (it.bio.isNullOrEmpty())
                tvBioUser.visibility = View.GONE
            else
                tvBioUser.text = it.bio

            if (it.company.isNullOrEmpty())
                llOrganisations.visibility = View.GONE
            else
                tvOrganizationsUser.text = it.company

            if (it.twitter_username.isNullOrEmpty())
                llTwitter.visibility = View.GONE
            else
                tvTwitterUser.text = it.twitter_username

            if (it.blog.isNullOrEmpty())
                llWebsite.visibility = View.GONE
            else
                tvWebsitesUser.text = it.blog

            if (it.location.isNullOrEmpty())
                llLocation.visibility = View.GONE
            else
                tvLocationUser.text = it.location

            tvJoinedUser.text = "Joined: ${it.created_at.subSequence(0, 10)}"
            tvFollowersUser.text = it.followers.toString()
            followersCount = it.followers
            tvFollowingUser.text = it.following.toString()
            tvRepositoriesUser.text =
                (it.public_repos).toString()

            Glide.with(baseContext)
                .load(it.avatar_url)
                .into(userAvatar)

            Glide.with(baseContext)
                .load(it.avatar_url)
                .into(userBackgroundImage)
        })
    }

    private fun getTopRepos() {

        viewModel.topRepositoryList.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                if (NotificationsProgressBar.visibility == View.VISIBLE)
                    NotificationsProgressBar.visibility = View.GONE
            }
            else {
                topRepoRecyclerViewUser.adapter = RepositoryAdapter(this, it)
            }
        })

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
