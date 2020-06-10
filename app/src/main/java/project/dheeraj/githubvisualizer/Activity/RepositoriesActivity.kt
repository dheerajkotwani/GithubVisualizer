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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_repositories.*
import project.dheeraj.githubvisualizer.Adapter.RepositoryAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.Network.GithubApiClient
import project.dheeraj.githubvisualizer.Network.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import project.dheeraj.githubvisualizer.ViewModel.RepositoriesViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class RepositoriesActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var call: Call<ArrayList<RepositoryModel>>
    private lateinit var username: String
    private lateinit var viewModel: RepositoriesViewModel
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)

        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        token = "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"

        viewModel = ViewModelProviders.of(this).get(RepositoriesViewModel::class.java)

        userName.text = intent.getStringExtra(AppConfig.LOGIN)

        buttonBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        if (intent.hasExtra("PAGE") && intent.getStringExtra("PAGE")=="STARS") {

            pageTitle.text = "Starred Repositories"

            if (intent.getStringExtra("USER_TYPE") == "user") {

                username = intent.getStringExtra(AppConfig.LOGIN)
                viewModel.getOtherStarredRepositories(token, username,1)
                getRepositories()

            } else {

                viewModel.getMyStarredRepositories(token, 1)
                getStarredRepositories()
            }
        }
        else {

            pageTitle.text = "Repositories"

            if (intent.getStringExtra("USER_TYPE" ) == "user") {

                username = intent.getStringExtra(AppConfig.LOGIN)
                viewModel.getOtherRepositories(token, username,1)
                getRepositories()

            }
            else {

                viewModel.getMyRepositories(token, 1)
                getRepositories()

            }
        }

    }


    private fun getRepositories() {

        viewModel.repoList.observe(this,  Observer {
            if (it.isNullOrEmpty()) {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
                if (repoProgressbar.visibility == View.VISIBLE)
                    repoProgressbar.visibility = View.GONE
            }
            else {
                repoRecyclerView.adapter = RepositoryAdapter(this, it)
                if (repoProgressbar.visibility == View.VISIBLE)
                    repoProgressbar.visibility = View.GONE
            }
        })

    }

    private fun getStarredRepositories() {

        viewModel.repoList.observe(this,  Observer {
            if (it.isNullOrEmpty()) {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
                if (repoProgressbar.visibility == View.VISIBLE)
                    repoProgressbar.visibility = View.GONE
            }
            else {
                repoRecyclerView.adapter = RepositoryAdapter(this, it)
                if (repoProgressbar.visibility == View.VISIBLE)
                    repoProgressbar.visibility = View.GONE
            }
        })

    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}


