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
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_developer_info.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_repository_info.*
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.content_repository_info.*
import kotlinx.android.synthetic.main.content_repository_info.profileProgressBar
import kotlinx.android.synthetic.main.content_repository_info.userInfoCard
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.llEmail
import kotlinx.android.synthetic.main.fragment_profile.llLocation
import kotlinx.android.synthetic.main.fragment_profile.llOrganisations
import kotlinx.android.synthetic.main.fragment_profile.llTwitter
import kotlinx.android.synthetic.main.fragment_profile.llWebsite
import kotlinx.android.synthetic.main.fragment_profile.tvDisplayName
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.Network.GithubApiClient
import project.dheeraj.githubvisualizer.Network.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import project.dheeraj.githubvisualizer.ViewModel.DeveloperViewModel
import project.dheeraj.githubvisualizer.ViewModel.RepositoryViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class DeveloperInfoActivity : AppCompatActivity() {

    private lateinit var sharedPref:SharedPreferences
    private lateinit var owner: String
    private lateinit var repo: String
    private lateinit var viewModel: DeveloperViewModel
    private lateinit var token: String
    private var star = false
    private var followCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_info)

        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)

        viewModel = ViewModelProviders.of(this).get(DeveloperViewModel::class.java)
        token = "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"

        Glide.with(this@DeveloperInfoActivity)
            .load(R.drawable.dheeraj_profile)
            .into(developerImage)

        llGithubDheeraj.setOnClickListener{
            var intent = Intent(this,
                ProfileActivity::class.java)
            intent.putExtra("login", "dheerajkotwani")
            startActivity(intent)
        }

        owner = "dheerajkotwani"
        repo = "GithubVisualizer"

        checkFollow()
        getUserData()
        getStar()
        viewModel.getStar(token, owner, repo)
        viewModel.getFollow(token, owner)

        followDheeraj.setOnClickListener {
            viewModel.putFollow(token, owner)
        }

        starGithubVisualiser.setOnClickListener {
            viewModel.putStar(token, owner, repo)
        }

    }

    private fun checkFollow(){

        viewModel.followData.observe(this, Observer {
            if (it == 404){
                followDheeraj.text = "Follow"
            }
            else if (it == 204 && followDheeraj.text == "Follow"){
                followDheeraj.text = "Following"
                followCount++
                tvFollowersDheeraj.text = followCount.toString()
            }
        })
    }

    private fun getUserData() {

        viewModel.getUserDetails(token, "dheerajkotwani")
        viewModel.userList.observe(this, Observer {
            followCount=  it.followers

            tvFollowersDheeraj.text = it.followers.toString()
            tvFollowingDheeraj.text = it.following.toString()
            tvRepositoriesDheeraj.text =
                (it.public_repos).toString()
        })

    }

    private fun getStar(){

        viewModel.starData.observe(this, Observer {
        if (it == 204 && !star) {
            star = true
            starGithubVisualiser.text = "Starred"
            starGithubVisualiser.isClickable = false
            Toast.makeText(this@DeveloperInfoActivity, "Starred", Toast.LENGTH_SHORT).show()
        }
        else if (it == 404) {
            star = false
            starGithubVisualiser.text = "Star"
        }
        else {
            star = false
            starGithubVisualiser.text = "Star"
            starGithubVisualiser.isClickable = false
            Toast.makeText(this@DeveloperInfoActivity, "Starred", Toast.LENGTH_SHORT).show()
        }
    })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
