package project.dheeraj.githubvisualizer.Activity

import FollowerModel
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_follow.*
import kotlinx.android.synthetic.main.activity_repositories.*
import kotlinx.android.synthetic.main.activity_repositories.buttonBack
import kotlinx.android.synthetic.main.activity_repositories.userName
import project.dheeraj.githubvisualizer.Adapter.FollowAdapter
import project.dheeraj.githubvisualizer.Adapter.RepositoryAdapter
import project.dheeraj.githubvisualizer.Adapter.SearchAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.GithubApiClient
import project.dheeraj.githubvisualizer.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlinx.android.synthetic.main.activity_repositories.pageTitle as pageTitle1

class FollowActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var call: Call<ArrayList<FollowerModel>>
    private var page = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow)


        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        userName.text = intent.getStringExtra(AppConfig.LOGIN)
        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);

        buttonBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        page = 1
        userName.text = intent.getStringExtra(AppConfig.LOGIN )

        if (intent.getStringExtra("PAGE" ) == "follower") {

            // TODO
            pageTitle.text = "Followers"
            call =
                apiInterface.getFollowers("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}",
                    intent.getStringExtra(AppConfig.LOGIN ), page)
            getFollow(apiInterface, call)

        }
        else {

            pageTitle.text = "Following"
            call =
                apiInterface.getFollowing("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}",
                    intent.getStringExtra(AppConfig.LOGIN ), page)
            getFollow(apiInterface, call)

        }

        followRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)
                    && (followProgressBar.visibility==View.GONE)) {
//                    Toast.makeText(context, "Last", Toast.LENGTH_LONG).show()
                    page++
                    getFollow(apiInterface, call)
                }
            }
        })

    }

    private fun getFollow(apiInterface: GithubApiInterface, call: Call<ArrayList<FollowerModel>>) {
        try {

            call.enqueue(object : Callback<ArrayList<FollowerModel>> {
                override fun onFailure(call: Call<ArrayList<FollowerModel>>, t: Throwable) {
                    Timber.e(t)
                }

                override fun onResponse(
                    call: Call<ArrayList<FollowerModel>>,
                    response: Response<ArrayList<FollowerModel>>
                ) {
                    var follow: ArrayList<FollowerModel> = ArrayList()

                    follow = response.body()!!

                    try {
                        followRecyclerView.adapter = FollowAdapter(this@FollowActivity, follow)
                        if (followProgressBar.visibility == View.VISIBLE)
                            followProgressBar.visibility = View.GONE
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
