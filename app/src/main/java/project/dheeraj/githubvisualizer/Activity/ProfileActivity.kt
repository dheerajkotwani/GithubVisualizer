package project.dheeraj.githubvisualizer.Activity

import GithubUserModel
import RepositoryModel
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.llEmail
import kotlinx.android.synthetic.main.fragment_profile.llLocation
import kotlinx.android.synthetic.main.fragment_profile.llOrganisations
import kotlinx.android.synthetic.main.fragment_profile.llTwitter
import kotlinx.android.synthetic.main.fragment_profile.llWebsite
import kotlinx.android.synthetic.main.fragment_profile.tvBio
import kotlinx.android.synthetic.main.fragment_profile.tvDisplayName
import kotlinx.android.synthetic.main.fragment_profile.tvEmail
import kotlinx.android.synthetic.main.fragment_profile.tvFollowers
import kotlinx.android.synthetic.main.fragment_profile.tvFollowing
import kotlinx.android.synthetic.main.fragment_profile.tvGists
import kotlinx.android.synthetic.main.fragment_profile.tvJoined
import kotlinx.android.synthetic.main.fragment_profile.tvLocation
import kotlinx.android.synthetic.main.fragment_profile.tvRepositories
import kotlinx.android.synthetic.main.fragment_profile.tvTwitter
import kotlinx.android.synthetic.main.fragment_profile.tvWebsites
import kotlinx.android.synthetic.main.fragment_profile.view.*
import project.dheeraj.githubvisualizer.Adapter.RepositoryAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.GithubApiClient
import project.dheeraj.githubvisualizer.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var username: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        username = intent.getStringExtra("login")
        supportActionBar!!.title = username
        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)

        Glide.with(baseContext)
            .load(intent.getStringExtra("avatar"))
            .into(userAvatar)

        Glide.with(baseContext)
            .load(intent.getStringExtra("avatar"))
            .into(userBackgroundImage)


        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);
        getUserData(apiInterface, username)
        getTopRepos(username)


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
            DynamicToast.makeWarning(this@ProfileActivity, "Developing").show()
        }


    }

    private fun getUserData(
        apiInterface: GithubApiInterface,
        username: String
    ) {
        var call: Call<GithubUserModel> =
            apiInterface.getPublicUser("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", username)
        try {
            call.enqueue(object : Callback<GithubUserModel> {
                override fun onFailure(call: Call<GithubUserModel>, t: Throwable) {
                    Toast.makeText(
                        baseContext,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<GithubUserModel>,
                    response: Response<GithubUserModel>
                ) {

                    try {

                        if (userInfoCard.visibility == View.GONE)
                            userInfoCard.visibility = View.VISIBLE
                        if (profileProgressBar.visibility == View.VISIBLE)
                            profileProgressBar.visibility = View.GONE

                        if (response.body()!!.name.isNullOrEmpty())
                            tvDisplayName.text = "Github User"
                        else
                            tvDisplayName.text = response.body()!!.name

                        if (response.body()!!.email.isNullOrEmpty())
                            llEmail.visibility = View.GONE
                        else
                            tvEmailUser.text = response.body()!!.email

                        if (response.body()!!.bio.isNullOrEmpty())
                            tvBioUser.visibility = View.GONE
                        else
                            tvBioUser.text = response.body()!!.bio

                        if (response.body()!!.company.isNullOrEmpty())
                            llOrganisations.visibility = View.GONE
                        else
                            tvOrganizationsUser.text = response.body()!!.company

                        if (response.body()!!.twitter_username.isNullOrEmpty())
                            llTwitter.visibility = View.GONE
                        else
                            tvTwitterUser.text = response.body()!!.twitter_username

                        if (response.body()!!.blog.isNullOrEmpty())
                            llWebsite.visibility = View.GONE
                        else
                            tvWebsitesUser.text = response.body()!!.blog

                        if (response.body()!!.location.isNullOrEmpty())
                            llLocation.visibility = View.GONE
                        else
                            tvLocationUser.text = response.body()!!.location

                        tvJoinedUser.text = "Joined: ${response.body()!!.created_at.subSequence(0, 10)}"
                        tvFollowersUser.text = response.body()!!.followers.toString()
                        tvFollowingUser.text = response.body()!!.following.toString()
                        tvGistsUser.text =
                            "${response.body()!!.public_gists}"
                        tvRepositoriesUser.text =
                            (response.body()!!.public_repos).toString()

                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun getTopRepos(
    username: String) {
        try {
            val apiInterface =
                GithubApiClient.getClient().create(GithubApiInterface::class.java);

            var call =
                apiInterface.topReposUser("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", username)
            call.enqueue(object : Callback<ArrayList<RepositoryModel>> {
                override fun onFailure(call: Call<ArrayList<RepositoryModel>>, t: Throwable) {
                    Timber.e(t)
                }

                override fun onResponse(
                    call: Call<ArrayList<RepositoryModel>>,
                    response: Response<ArrayList<RepositoryModel>>
                ) {
                    var repos: ArrayList<RepositoryModel> = ArrayList()


                    try {

                        repos = response.body()!!

                        topRepoRecyclerViewUser.adapter = RepositoryAdapter(this@ProfileActivity, repos)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }

                }

            })

        } catch (e: Exception) {
            Timber.e(e)
        }
    }

}
