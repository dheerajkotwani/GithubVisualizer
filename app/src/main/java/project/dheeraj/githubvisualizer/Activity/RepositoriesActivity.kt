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

import RepositoryModel
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_follow.*
import kotlinx.android.synthetic.main.activity_repositories.*
import kotlinx.android.synthetic.main.activity_repositories.buttonBack
import kotlinx.android.synthetic.main.activity_repositories.pageTitle
import kotlinx.android.synthetic.main.activity_repositories.userName
import project.dheeraj.githubvisualizer.Adapter.RepositoryAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.R
import project.dheeraj.githubvisualizer.ViewModel.RepositoriesViewModel
import retrofit2.Call

class RepositoriesActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var username: String
    private lateinit var viewModel: RepositoriesViewModel
    private lateinit var repoList: ArrayList<RepositoryModel>
    private lateinit var adapter: RepositoryAdapter
    private lateinit var token: String
    private var page: Int = 1
    private var pageType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)

        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        token = "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"
        repoList = ArrayList()
        adapter = RepositoryAdapter(this, repoList)
        repoRecyclerView.adapter = adapter
        userName.text = intent.getStringExtra(AppConfig.LOGIN)

        viewModel = ViewModelProviders.of(this).get(RepositoriesViewModel::class.java)

        Glide.with(this)
            .load(R.drawable.github_loader)
            .into(gitProgressbar)

        buttonBack.setOnClickListener {
            onBackPressed()
            finish()
        }
        if (intent.hasExtra(AppConfig.LOGIN))
            username = intent.getStringExtra(AppConfig.LOGIN)
        else
            username = "${sharedPref.getString(AppConfig.LOGIN, "")}"

        getRepositories()

        if (intent.hasExtra("PAGE") && intent.getStringExtra("PAGE")=="STARS") {

            pageTitle.text = "Starred Repositories"

            if (intent.getStringExtra("USER_TYPE") == "user") {
                pageType = 1

                viewModel.getOtherStarredRepositories(token, username,page)

            } else {
                pageType = 2
                viewModel.getMyStarredRepositories(token, page)
            }
        }
        else {

            pageTitle.text = "Repositories"

            if (intent.getStringExtra("USER_TYPE" ) == "user") {
                pageType = 3

                viewModel.getOtherRepositories(token, username, page)

            }
            else {

                pageType = 4
                viewModel.getMyRepositories(token, page)

            }
        }

        buttonLoadMoreRepos.setOnClickListener {
            page++
            when (pageType){
                1 -> viewModel.getOtherStarredRepositories(token, username,page)
                2 -> viewModel.getMyStarredRepositories(token, page)
                3 -> viewModel.getOtherRepositories(token, username, page)
                4 -> viewModel.getMyRepositories(token, page)
            }
        }

    }


    private fun getRepositories() {

        viewModel.repoList.observe(this,  Observer {
            if (it.isNullOrEmpty()) {
                Toast.makeText(this, "No more items", Toast.LENGTH_SHORT).show()
                if (gitProgressbar.visibility == View.VISIBLE)
                    gitProgressbar.visibility = View.GONE
                if (buttonLoadMoreRepos.isVisible)
                    buttonLoadMoreRepos.visibility = View.GONE
            }
            else {
//                repoRecyclerView.adapter = RepositoryAdapter(this@RepositoriesActivity, it)
                repoList.addAll(it)
                adapter.notifyDataSetChanged()
                if (!buttonLoadMoreRepos.isVisible)
                    buttonLoadMoreRepos.visibility = View.VISIBLE
                if (gitProgressbar.visibility == View.VISIBLE)
                    gitProgressbar.visibility = View.GONE
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}


