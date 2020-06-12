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

package project.dheeraj.githubvisualizer.Fragment.Repository

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_repository_info.*
import kotlinx.android.synthetic.main.fragment_repo_info.*
import project.dheeraj.githubvisualizer.Activity.TestRepoActivity
import project.dheeraj.githubvisualizer.AppConfig

import project.dheeraj.githubvisualizer.R
import project.dheeraj.githubvisualizer.ViewModel.RepositoryViewModel
import timber.log.Timber
import java.lang.Exception

class RepoInfoFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var owner: String
    private lateinit var repo: String
    private lateinit var viewModel: RepositoryViewModel
    private lateinit var token: String
    private var starCount: Int = 0
    private var star = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repo_info, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedPref = context!!.getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)

        owner = "${sharedPref.getString(AppConfig.LOGIN, "")}"
        repo = "GithubVisualizer"
        token = "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"
        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel::class.java)

        owner = arguments!!.getString("owner", "")
        repo = arguments!!.getString("repo", "")


        getRepoData()
        observeReadme()
//        observeStar()
//        viewModel.getStar(token, owner, repo)

//        buttonStar.setOnClickListener {
//            buttonStar.isClickable = false
//            if (star){
//                viewModel.removeStar(token, owner, repo)
//            }
//            else
//                viewModel.putStar(token, owner, repo)
//        }

        tvWatchersRepo.setOnClickListener {
            val intent = Intent(context, TestRepoActivity::class.java)
            intent.putExtra("repo", repo)
            intent.putExtra("owner", owner)
            startActivity(intent)
        }
    }


    private fun getRepoData() {

        viewModel.repoData.observe(viewLifecycleOwner, Observer {
            tvRepoName.text = it.full_name
            tvRepoDescription.text = it.description
            tvForksRepo.text = it.forks_count.toString()
            tvStaggersRepo.text = it.stargazers_count.toString()
            starCount = it.stargazers_count
            tvWatchersRepo.text = it.watchers_count.toString()
            tvIssuesRepo.text = it.open_issues_count.toString()

//            if (buttonStar.visibility == View.GONE)
//                buttonStar.visibility = View.VISIBLE

            if (userInfoCard.visibility == View.GONE)
                userInfoCard.visibility = View.VISIBLE

//            buttonStar.isClickable = true

//            Glide.with(this)
//                .load(it.owner.avatar_url)
//                .into(repoBackgroundImage)
        })


        viewModel.repoDetails(token, owner, repo)

    }

    private fun observeReadme() {

        try {
            viewModel.getReadme(token, owner, repo)
        }
        catch (e: Exception) {
            Timber.e(e)
        }

        viewModel.readmeData.observe(viewLifecycleOwner, Observer {

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
                repoWebView.settings.layoutAlgorithm =
                    WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
                repoWebView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                repoWebView.settings.setSupportZoom(true)
                repoWebView.settings.builtInZoomControls = true
                repoWebView.settings.displayZoomControls = true
            }

        })
    }
/*
    private fun observeStar() {
        viewModel.starData.observe(viewLifecycleOwner, Observer {
            if (it == 204 && !buttonStar.isVisible) {
                star = true
                buttonStar.setImageResource(R.drawable.ic_star_black_24dp)
                buttonStar.setColorFilter(
                    ContextCompat.getColor(context!!, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN)
            }
            else if (it == 404 && !buttonStar.isVisible){
                star = false
                buttonStar.setImageResource(R.drawable.ic_star_border_black_24dp)
                buttonStar.setColorFilter(
                    ContextCompat.getColor(context!!, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN)
            }
            else if (it == 204 && buttonStar.isVisible && !star) {
                star = true
                buttonStar.setImageResource(R.drawable.ic_star_black_24dp)
                buttonStar.setColorFilter(
                    ContextCompat.getColor(context!!, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN)
                starCount+=1
                tvStaggersRepo.text = starCount.toString()
                buttonStar.isClickable = true
                Toast.makeText(context!!, "Starred", Toast.LENGTH_SHORT).show()
            }
            else if (it == 204 && buttonStar.isVisible && star){
                star = false
                buttonStar.setImageResource(R.drawable.ic_star_border_black_24dp)
                buttonStar.setColorFilter(
                    ContextCompat.getColor(context!!, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN)
                starCount-=1
                tvStaggersRepo.text = starCount.toString()
                buttonStar.isClickable = true
                Toast.makeText(context!!, "Star Removed", Toast.LENGTH_SHORT).show()
            }
            else {
                buttonStar.isClickable = false
                if (buttonStar.isVisible)
                    buttonStar.isVisible = false
                Toast.makeText(context!!, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

 */

}
