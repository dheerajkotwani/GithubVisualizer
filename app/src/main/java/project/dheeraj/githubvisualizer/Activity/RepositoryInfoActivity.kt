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

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.webkit.WebSettings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_repository_info.*
import kotlinx.android.synthetic.main.content_repository_info.*
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.R
import project.dheeraj.githubvisualizer.ViewModel.RepositoryViewModel


class RepositoryInfoActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var owner: String
    private lateinit var repo: String
    private lateinit var viewModel: RepositoryViewModel
    private lateinit var token: String
    private var starCount: Int = 0
    private var star = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_info)
        setSupportActionBar(toolbar)

        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)

        owner = "${sharedPref.getString(AppConfig.LOGIN, "")}"
        repo = "${sharedPref.getString(AppConfig.LOGIN, "")}"
        token = "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"
        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel::class.java)

        if (intent.hasExtra("owner"))
           owner = intent.getStringExtra("owner")
        if (intent.hasExtra("repo")) {
            repo = intent.getStringExtra("repo")
            supportActionBar!!.title = repo
        }

        getRepoData()
        observeReadme()
        observeStar()
        viewModel.getStar(token, owner, repo)

        buttonStar.setOnClickListener {
            buttonStar.isClickable = false
            if (star){
                viewModel.removeStar(token, owner, repo)
            }
            else
                viewModel.putStar(token, owner, repo)
        }

    }


    private fun getRepoData() {

        viewModel.repoData.observe(this, Observer {
            tvRepoName.text = it.full_name
            tvRepoDescription.text = it.description
            tvForksRepo.text = it.forks_count.toString()
            tvStaggersRepo.text = it.stargazers_count.toString()
            starCount = it.stargazers_count
            tvWatchersRepo.text = it.watchers_count.toString()
            tvIssuesRepo.text = it.open_issues_count.toString()

            if (buttonStar.visibility == View.GONE)
                buttonStar.visibility = View.VISIBLE

            if (userInfoCard.visibility == View.GONE)
                userInfoCard.visibility = View.VISIBLE

            buttonStar.isClickable = true

            Glide.with(this@RepositoryInfoActivity)
                .load(it.owner.avatar_url)
                .into(repoBackgroundImage)
        })


        viewModel.repoDetails(token, owner, repo)

    }

    private fun observeReadme() {

        viewModel.getReadme(token, owner, repo)

        viewModel.readmeData.observe(this, Observer {

            profileProgressBar.visibility = View.GONE
            if (!it.download_url.isNullOrEmpty()) {

                repoWebView.loadFromUrl(it.download_url)

//                repoWebView.setOnTouchListener(OnTouchListener { v, event -> event.action == MotionEvent.ACTION_MOVE })
                repoWebView.isVerticalScrollBarEnabled = false
                repoWebView.settings.javaScriptEnabled = true;
                repoWebView.settings.domStorageEnabled = true
                repoWebView.settings.setAppCacheEnabled(true);
                repoWebView.settings.loadsImagesAutomatically = true;
                repoWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
                repoWebView.isCodeScrollEnabled = true
                repoWebView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
                repoWebView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                repoWebView.settings.setSupportZoom(true)
                repoWebView.settings.builtInZoomControls = true
                repoWebView.settings.displayZoomControls = false
            }

        })
    }

    private fun observeStar() {
        viewModel.starData.observe(this, Observer {
            if (it == 204 && !buttonStar.isVisible) {
                star = true
                buttonStar.setImageResource(R.drawable.ic_star_black_24dp)
                buttonStar.setColorFilter(
                    ContextCompat.getColor(this@RepositoryInfoActivity, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN)
            }
            else if (it == 404 && !buttonStar.isVisible){
                star = false
                buttonStar.setImageResource(R.drawable.ic_star_border_black_24dp)
                buttonStar.setColorFilter(
                    ContextCompat.getColor(this@RepositoryInfoActivity, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN)
            }
            else if (it == 204 && buttonStar.isVisible && !star) {
                star = true
                buttonStar.setImageResource(R.drawable.ic_star_black_24dp)
                buttonStar.setColorFilter(
                    ContextCompat.getColor(this@RepositoryInfoActivity, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN)
                starCount+=1
                tvStaggersRepo.text = starCount.toString()
                buttonStar.isClickable = true
                Toast.makeText(this, "Starred", Toast.LENGTH_SHORT).show()
            }
            else if (it == 204 && buttonStar.isVisible && star){
                star = false
                buttonStar.setImageResource(R.drawable.ic_star_border_black_24dp)
                buttonStar.setColorFilter(
                    ContextCompat.getColor(this@RepositoryInfoActivity, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN)
                starCount-=1
                tvStaggersRepo.text = starCount.toString()
                buttonStar.isClickable = true
                Toast.makeText(this, "Star Removed", Toast.LENGTH_SHORT).show()
            }
            else {
                buttonStar.isClickable = false
                if (buttonStar.isVisible)
                    buttonStar.isVisible = false
                Toast.makeText(this@RepositoryInfoActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}
