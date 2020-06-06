package project.dheeraj.githubvisualizer.Fragment.Main

import GithubUserModel
import RepositoryModel
import android.content.Context
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
import project.dheeraj.githubvisualizer.*
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
        profileImage = view.findViewById(R.id.imageView_displayImage)
        tvDisplayName = view.findViewById(R.id.text_name)
        tvUserName = view.findViewById(R.id.text_username)
        tvBio = view.findViewById(R.id.text_bio)
//        tvStatus = view.findViewById(R.id.text_status)
        tvOrganization = view.findViewById(R.id.text_organizaions)
        tvEmail = view.findViewById(R.id.text_email)
        tvWebsite = view.findViewById(R.id.text_website)
        tvJoined = view.findViewById(R.id.text_joined)
        tvFollowers = view.findViewById(R.id.text_followers)
        tvFollowing = view.findViewById(R.id.text_following)
        tvRepositories = view.findViewById(R.id.text_repo)
        tvTwitter = view.findViewById(R.id.text_twitter)
        tvStars = view.findViewById(R.id.text_stars)
        tvLocation = view.findViewById(R.id.text_location)
        recyclerViewTopRepo = view.findViewById(R.id.toprepo_recyclerView)
        sharedPref = context!!.getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)


        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);
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

                        tvDisplayName.text = response.body()!!.name
                        tvEmail.text = response.body()!!.email
//                tvStatus.text = response.body()!!.
                        tvBio.text = response.body()!!.bio
                        tvOrganization.text = response.body()!!.company
                        tvTwitter.text = response.body()!!.twitter_username
                        tvJoined.text = "Joined: ${response.body()!!.created_at.subSequence(0, 10)}"
                        tvFollowers.text = response.body()!!.followers.toString()
                        tvFollowing.text = response.body()!!.following.toString()
                        tvLocation.text = response.body()!!.location.toString()
                        tvStars.text =
                            "${response.body()!!.public_gists + response.body()!!.private_gists}"
                        tvRepositories.text =
                            (response.body()!!.public_repos + response.body()!!.total_private_repos).toString()
                        tvWebsite.text = response.body()!!.blog

//                context?.let {
                        Glide.with(view)
                            .load(response.body()!!.avatar_url)
                            .into(profileImage)
//                }
                    }
                    catch(e: Exception){
                        Timber.e(e)
                    }
                }
            })
        }
        catch (e: Exception){
            Timber.e(e)
        }


        try {

            var call = apiInterface.topRepos("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}")
            call.enqueue(object: Callback<ArrayList<RepositoryModel>>{
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
                    }
                    catch(e: Exception) {
                        Timber.e(e)
                    }

                }

            })

        }
        catch(e: Exception){
            Timber.e(e)
        }


        return view;
    }

    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
    }


}
