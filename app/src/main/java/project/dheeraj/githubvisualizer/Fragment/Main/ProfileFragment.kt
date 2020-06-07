package project.dheeraj.githubvisualizer.Fragment.Main

import GithubUserModel
import RepositoryModel
import StarredModel
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import project.dheeraj.githubvisualizer.*
import project.dheeraj.githubvisualizer.Activity.FollowActivity
import project.dheeraj.githubvisualizer.Activity.RepositoriesActivity
import project.dheeraj.githubvisualizer.Adapter.RepositoryAdapter

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ProfileFragment : Fragment(), FragmentLifecycle {

    private lateinit var profileImage: ImageView
    private lateinit var tvDisplayName: TextView
    private lateinit var tvUserName: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvBio: TextView
    private lateinit var tvOrganization: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvWebsite: TextView
    private lateinit var tvTwitter: TextView
    private lateinit var tvJoined: TextView
    private lateinit var tvFollowers: TextView
    private lateinit var tvFollowing: TextView
    private lateinit var tvRepositories: TextView
    private lateinit var tvStars: TextView
    private lateinit var tvLocation: TextView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var mainView: View
    private lateinit var recyclerViewTopRepo: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        mainView = view
        // Initialize View
        profileImage = view.findViewById(R.id.profileImage)
        tvDisplayName = view.findViewById(R.id.tvDisplayName)
        tvUserName = view.findViewById(R.id.tvUserName)
        tvBio = view.findViewById(R.id.tvBio)
        tvOrganization = view.findViewById(R.id.tvDescriptions)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvWebsite = view.findViewById(R.id.tvWebsites)
        tvJoined = view.findViewById(R.id.tvJoined)
        tvFollowers = view.findViewById(R.id.tvFollowers)
        tvFollowing = view.findViewById(R.id.tvFollowing)
        tvRepositories = view.findViewById(R.id.tvRepositories)
        tvTwitter = view.findViewById(R.id.tvTwitter)
        tvStars = view.findViewById(R.id.tvGists)
        tvLocation = view.findViewById(R.id.tvLocation)
        recyclerViewTopRepo = view.findViewById(R.id.topRepoRecyclerView)
        sharedPref = context!!.getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)


        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);

        fetchStars(apiInterface)

        getUserData(apiInterface, view)

        getTopRepos(apiInterface)


        // TODO
        view.llFollowers.setOnClickListener {
//            DynamicToast.makeWarning(context!!, "Developing").show()
            val intent = Intent(context, FollowActivity::class.java)
            intent.putExtra(AppConfig.LOGIN, sharedPref.getString(AppConfig.LOGIN, "User"))
            intent.putExtra("PAGE", "follower")
            startActivity(intent)
        }

        view.llFollowing.setOnClickListener {
//            DynamicToast.makeWarning(context!!, "Developing").show()
            val intent = Intent(context, FollowActivity::class.java)
            intent.putExtra(AppConfig.LOGIN, sharedPref.getString(AppConfig.LOGIN, "User"))
            intent.putExtra("PAGE", "following")
            startActivity(intent)
        }

        view.llRepo.setOnClickListener {
//            DynamicToast.makeWarning(context!!, "Developing").show()
            val intent = Intent(context, RepositoriesActivity::class.java)
            intent.putExtra(AppConfig.LOGIN, sharedPref.getString(AppConfig.LOGIN, "User"))
            intent.putExtra("USER_TYPE", "me")
            intent.putExtra("PAGE", "REPO")
            startActivity(intent)
        }

        // TODO
        view.llGists.setOnClickListener {

            val intent = Intent(context, RepositoriesActivity::class.java)
            intent.putExtra(AppConfig.LOGIN, sharedPref.getString(AppConfig.LOGIN, "User"))
            intent.putExtra("USER_TYPE", "me")
            intent.putExtra("PAGE", "STARS")
            startActivity(intent)
//            DynamicToast.makeWarning(context!!, "Developing").show()
        }

        // TODO
        view.profileImage.setOnClickListener {
            DynamicToast.makeWarning(context!!, "Developing").show()
        }

        return view;
    }

    private fun getTopRepos(apiInterface: GithubApiInterface) {
        try {

            var call =
                apiInterface.topRepos("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}")
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
                        recyclerViewTopRepo.adapter = RepositoryAdapter(context!!, repos)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }

                }

            })

        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun getUserData(
        apiInterface: GithubApiInterface,
        view: View
    ) {
        var call: Call<GithubUserModel> =
            apiInterface.getUserInfo("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}")
        try {
            call.enqueue(object : Callback<GithubUserModel> {
                override fun onFailure(call: Call<GithubUserModel>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<GithubUserModel>,
                    response: Response<GithubUserModel>
                ) {

                    try {

                        if (profileUserProgressBar.visibility == View.VISIBLE)
                            profileUserProgressBar.visibility = View.GONE

                        if (response.body()!!.name.isNullOrEmpty())
                            tvDisplayName.text = "Github User"
                        else
                            tvDisplayName.text = response.body()!!.name

                        if (response.body()!!.email.isNullOrEmpty())
                            llEmail.visibility = View.GONE
                        else
                            tvEmail.text = response.body()!!.email

                        if (response.body()!!.bio.isNullOrEmpty())
                            tvBio.visibility = View.GONE
                        else
                            tvBio.text = response.body()!!.bio

                        if (response.body()!!.company.isNullOrEmpty())
                            llOrganisations.visibility = View.GONE
                        else
                            tvOrganization.text = response.body()!!.company

                        if (response.body()!!.twitter_username.isNullOrEmpty())
                            llTwitter.visibility = View.GONE
                        else
                            tvTwitter.text = response.body()!!.twitter_username

                        if (response.body()!!.blog.isNullOrEmpty())
                            llWebsite.visibility = View.GONE
                        else
                            tvWebsite.text = response.body()!!.blog

                        if (response.body()!!.location.isNullOrEmpty())
                            llLocation.visibility = View.GONE
                        else
                            tvLocation.text = response.body()!!.location

                        view.tvUserName.text = response.body()!!.login
                        tvJoined.text = "Joined: ${response.body()!!.created_at.subSequence(0, 10)}"
                        tvFollowers.text = response.body()!!.followers.toString()
                        tvFollowing.text = response.body()!!.following.toString()
//                        tvStars.text =
//                            "${response.body()!!.public_gists + response.body()!!.private_gists}"
                        tvRepositories.text =
                            (response.body()!!.public_repos + response.body()!!.total_private_repos).toString()

                        sharedPref.edit()
                            .putString(AppConfig.NAME, response.body()!!.name)
                            .putString(AppConfig.LOGIN, response.body()!!.login)
                            .apply()



                        Glide.with(view)
                            .load(response.body()!!.avatar_url)
                            .into(profileImage)

                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun fetchStars(
        apiInterface: GithubApiInterface
    ) {

        var call: Call<ArrayList<RepositoryModel>> =
            apiInterface.starredRepo(
                "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"
            )
        try {


            call.enqueue(object : Callback<ArrayList<RepositoryModel>> {
                override fun onFailure(call: Call<ArrayList<RepositoryModel>>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<ArrayList<RepositoryModel>>,
                    response: Response<ArrayList<RepositoryModel>>
                ) {
                    try {

                        tvStars.text = "${response.body()!!.size}"

                    } catch (e: Exception) {
                        Timber.e(e)

                    }
                }
            })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
    }


}
