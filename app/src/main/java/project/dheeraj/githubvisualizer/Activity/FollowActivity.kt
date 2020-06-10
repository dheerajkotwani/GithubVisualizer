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

import FollowerModel
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_follow.*
import kotlinx.android.synthetic.main.activity_repositories.buttonBack
import kotlinx.android.synthetic.main.activity_repositories.userName
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_profile.*
import project.dheeraj.githubvisualizer.Adapter.FollowAdapter
import project.dheeraj.githubvisualizer.Adapter.RepositoryAdapter
import project.dheeraj.githubvisualizer.Adapter.SearchAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.Network.GithubApiClient
import project.dheeraj.githubvisualizer.Network.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import project.dheeraj.githubvisualizer.ViewModel.FollowViewModel
import project.dheeraj.githubvisualizer.ViewModel.ProfileViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class FollowActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private var page = 1
    private lateinit var viewModel: FollowViewModel
    private lateinit var token: String
    private var followerPage: Boolean = false
    private lateinit var adapter: FollowAdapter
    private lateinit var followList: ArrayList<FollowerModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow)


        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        userName.text = intent.getStringExtra(AppConfig.LOGIN)
        viewModel = ViewModelProviders.of(this).get(FollowViewModel::class.java)
        token = "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"
        followList = ArrayList()
        adapter = FollowAdapter(this, followList)
        followRecyclerView.adapter = adapter

        Glide.with(this)
            .load(R.drawable.github_loader)
            .into(followProgressBar)

        buttonBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        page = 1
        userName.text = intent.getStringExtra(AppConfig.LOGIN )

        followerPage = intent.getStringExtra("PAGE" ) == "follower"

        if (followerPage) {

            pageTitle.text = "Followers"
            viewModel.getFollowers(token, userName.text.toString(), page)
            getFollow()

        }
        else {

            pageTitle.text = "Following"
            viewModel.getFollowers(token, userName.text.toString(), page)
            getFollow()

        }

        buttonLoadMore.setOnClickListener {

            buttonLoadMore.isClickable = false
            page++
            if (followerPage) {

                viewModel.getFollowers(token, userName.text.toString(), page)
                getFollow()

            }
            else {

                viewModel.getFollowers(token, userName.text.toString(), page)
                getFollow()

            }

        }

    }

    private fun getFollow() {

        viewModel.followList.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
                if (followProgressBar.visibility == View.VISIBLE)
                    followProgressBar.visibility = View.GONE
            }
            else {

                if (it.size==100){

                    buttonLoadMore.visibility = View.VISIBLE
                    buttonLoadMore.isClickable = true
                }

                followList.addAll(it)

                if (followProgressBar.visibility == View.VISIBLE)
                    followProgressBar.visibility = View.GONE

                adapter.notifyDataSetChanged()

            }
        })


    }

}
