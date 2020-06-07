package project.dheeraj.githubvisualizer.Activity

import Readme
import RepositoryModel
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_developer_info.*
import kotlinx.android.synthetic.main.activity_repository_info.*
import kotlinx.android.synthetic.main.content_repository_info.*
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.GithubApiClient
import project.dheeraj.githubvisualizer.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class RepositoryInfoActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var owner: String
    private lateinit var repo: String
    private var star = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_info)
        setSupportActionBar(toolbar)

        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);

        owner = "${sharedPref.getString(AppConfig.LOGIN, "")}"
        repo = "${sharedPref.getString(AppConfig.LOGIN, "")}"

        if (intent.hasExtra("owner"))
           owner = intent.getStringExtra("owner")
        if (intent.hasExtra("repo")) {
            repo = intent.getStringExtra("repo")
            supportActionBar!!.title = repo
        }

        getRepoData(apiInterface, owner)
        getRepoReadme(apiInterface, owner)
        getStar(apiInterface)

        buttonStar.setOnClickListener {
            if (star){
                removeStar(apiInterface)
            }
            else
                putStar(apiInterface)
        }

    }


    private fun getRepoData(
        apiInterface: GithubApiInterface,
        username: String
    ) {
        var call: Call<RepositoryModel> =
            apiInterface.getReposData("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", owner, repo)
        try {
            call.enqueue(object : Callback<RepositoryModel> {
                override fun onFailure(call: Call<RepositoryModel>, t: Throwable) {
                    Toast.makeText(
                        baseContext,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<RepositoryModel>,
                    response: Response<RepositoryModel>
                ) {

                    try {


                        tvRepoName.text = response.body()!!.full_name
                        tvRepoDescription.text = response.body()!!.description
                        tvForksRepo.text = response.body()!!.forks_count.toString()
                        tvStaggersRepo.text = response.body()!!.stargazers_count.toString()
                        tvWatchersRepo.text = response.body()!!.watchers_count.toString()
                        tvIssuesRepo.text = response.body()!!.open_issues_count.toString()

                        Glide.with(this@RepositoryInfoActivity)
                            .load(response.body()!!.owner.avatar_url)
                            .into(repoBackgroundImage)


                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun getRepoReadme(
        apiInterface: GithubApiInterface,
        username: String
    ) {
        var call: Call<Readme> =
            apiInterface.getReposReadme("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", owner, repo)
        try {
            call.enqueue(object : Callback<Readme> {
                override fun onFailure(call: Call<Readme>, t: Throwable) {
                    Toast.makeText(
                        baseContext,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<Readme>,
                    response: Response<Readme>
                ) {

                    try {
                        profileProgressBar.visibility = View.GONE
                        repoWebView.loadFromUrl(response.body()!!.download_url)

                        repoWebView.setOnTouchListener(OnTouchListener { v, event -> event.action == MotionEvent.ACTION_MOVE })
                        repoWebView.isVerticalScrollBarEnabled = false

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
                            buttonStar.setImageResource(R.drawable.ic_star_black_24dp)
                            buttonStar.setColorFilter(
                                ContextCompat.getColor(this@RepositoryInfoActivity, R.color.yellow_600),
                                android.graphics.PorterDuff.Mode.SRC_IN)
                        }
                        else {
                            star = false
                            buttonStar.setImageResource(R.drawable.ic_star_border_black_24dp)
                            buttonStar.setColorFilter(
                                ContextCompat.getColor(this@RepositoryInfoActivity, R.color.white),
                                android.graphics.PorterDuff.Mode.SRC_IN)
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
                            buttonStar.setImageResource(R.drawable.ic_star_black_24dp)
                            buttonStar.setColorFilter(
                                ContextCompat.getColor(this@RepositoryInfoActivity, R.color.yellow_600),
                                android.graphics.PorterDuff.Mode.SRC_IN)
                        }
                        else {
                            star = false
                            buttonStar.setImageResource(R.drawable.ic_star_border_black_24dp)
                            buttonStar.setColorFilter(
                                ContextCompat.getColor(this@RepositoryInfoActivity, R.color.white),
                                android.graphics.PorterDuff.Mode.SRC_IN)
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

    private fun removeStar(
        apiInterface: GithubApiInterface
    ) {
        var call: Call<String> =
            apiInterface.removeStar("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", owner, repo)
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
                            star = false
                            buttonStar.setImageResource(R.drawable.ic_star_border_black_24dp)
                            buttonStar.setColorFilter(
                                ContextCompat.getColor(this@RepositoryInfoActivity, R.color.white),
                                android.graphics.PorterDuff.Mode.SRC_IN)
                        }
                        else {
                            Toast.makeText(this@RepositoryInfoActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
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
