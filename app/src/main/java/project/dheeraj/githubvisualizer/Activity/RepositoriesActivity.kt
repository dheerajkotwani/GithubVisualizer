package project.dheeraj.githubvisualizer.Activity

import RepositoryModel
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_repositories.*
import project.dheeraj.githubvisualizer.Adapter.RepositoryAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.GithubApiClient
import project.dheeraj.githubvisualizer.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class RepositoriesActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var call: Call<ArrayList<RepositoryModel>>
    private lateinit var username: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)

        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        userName.text = intent.getStringExtra(AppConfig.LOGIN)
        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);

        buttonBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        if (intent.hasExtra("PAGE") && intent.getStringExtra("PAGE")=="STARS") {

            pageTitle.text = "Starred Repositories"

            if (intent.getStringExtra("USER_TYPE") == "user") {

                // TODO
                username = intent.getStringExtra(AppConfig.LOGIN)
                call =
                    apiInterface.starredRepoOfUser(
                        "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}",
                        username
                    )
                getStarredRepo(apiInterface, call)

            } else {

                call =
                    apiInterface.starredRepo(
                        "token ${sharedPref.getString(
                            AppConfig.ACCESS_TOKEN,
                            ""
                        )}"
                    )
                getStarredRepo(apiInterface, call)

            }
        }
        else {

            pageTitle.text = "Repositories"

            if (intent.getStringExtra("USER_TYPE" ) == "user") {

                // TODO
                username = intent.getStringExtra(AppConfig.LOGIN)
                call =
                    apiInterface.getUserRepos("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}",
                        username)
                getRepositories(apiInterface, call)

            }
            else {

                call =
                    apiInterface.getMyRepos("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}")
                getRepositories(apiInterface, call)

            }
        }

    }

    private fun getRepositories(apiInterface: GithubApiInterface, call: Call<ArrayList<RepositoryModel>>) {
        try {

            call.enqueue(object : Callback<ArrayList<RepositoryModel>> {
                override fun onFailure(call: Call<ArrayList<RepositoryModel>>, t: Throwable) {
                    Timber.e(t)
                }

                override fun onResponse(
                    call: Call<ArrayList<RepositoryModel>>,
                    response: Response<ArrayList<RepositoryModel>>
                ) {
                    var repos: ArrayList<RepositoryModel> = ArrayList()

                    repos = response.body()!!

                    try {
                        repoRecyclerView.adapter = RepositoryAdapter(this@RepositoriesActivity, repos)
                        if (repoProgressbar.visibility == View.VISIBLE)
                        repoProgressbar.visibility = View.GONE
                    } catch (e: Exception) {
                        Timber.e(e)
                    }

                }

            })

        } catch (e: Exception) {
            Timber.e(e)
        }
    }


    private fun getStarredRepo(apiInterface: GithubApiInterface, call: Call<ArrayList<RepositoryModel>>) {
        try {

            call.enqueue(object : Callback<ArrayList<RepositoryModel>> {
                override fun onFailure(call: Call<ArrayList<RepositoryModel>>, t: Throwable) {
                    Timber.e(t)
                }

                override fun onResponse(
                    call: Call<ArrayList<RepositoryModel>>,
                    response: Response<ArrayList<RepositoryModel>>
                ) {
                    var repos: ArrayList<RepositoryModel> = ArrayList()

                    repos = response.body()!!

                    try {
                        repoRecyclerView.adapter = RepositoryAdapter(this@RepositoriesActivity, repos)
                        if (repoProgressbar.visibility == View.VISIBLE)
                            repoProgressbar.visibility = View.GONE
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


