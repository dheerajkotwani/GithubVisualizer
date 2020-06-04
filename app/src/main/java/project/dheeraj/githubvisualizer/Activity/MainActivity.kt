package project.dheeraj.githubvisualizer.Activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import kotlinx.android.synthetic.main.activity_main.*
import project.dheeraj.githubvisualizer.*
import project.dheeraj.githubvisualizer.Adapter.ViewPagerAdapter
import project.dheeraj.githubvisualizer.AppConfig.ACCESS_TOKEN
import project.dheeraj.githubvisualizer.AppConfig.SHARED_PREF
import project.dheeraj.githubvisualizer.Fragment.Main.*
import project.dheeraj.githubvisualizer.Model.FragmentModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    private lateinit var apiInterface: GithubApiInterface
    private lateinit var sharedPref: SharedPreferences
    private lateinit var mainBottomNavigation: MeowBottomNavigation
    private lateinit var mainViewPager: ViewPager
    private lateinit var  fragmentModel: ArrayList<FragmentModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initalizeViews()

        setBottomNavigation()

        setViewPager()


        mainBottomNavigation.setOnShowListener {
            // YOUR CODES
        }

        mainBottomNavigation.setOnClickMenuListener {
            // YOUR CODES
            mainViewPager.currentItem = it.id-1

        }

        apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);
        var call: Call<ProfileModel> =
            apiInterface.getUserInfo("token ${sharedPref.getString(ACCESS_TOKEN, "")}")
        call.enqueue(object : Callback<ProfileModel> {
            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<ProfileModel>, response: Response<ProfileModel>) {
                Toast.makeText(
                    this@MainActivity,
                    "response: ${response.body()!!.bio}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun setViewPager() {
        val pagerAdapter = ViewPagerAdapter(supportFragmentManager, fragmentModel)
        mainViewPager.adapter = pagerAdapter
        mainViewPager.currentItem = 2
    }

    private fun setBottomNavigation() {
        mainBottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_home_black_24dp))
        mainBottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_search_black_24dp))
        mainBottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.ic_github_logo))
        mainBottomNavigation.add(MeowBottomNavigation.Model(4, R.drawable.ic_notifications_none_black_24dp))
        mainBottomNavigation.add(MeowBottomNavigation.Model(5, R.drawable.ic_person_black_24dp))

        mainBottomNavigation.show(3)

        fragmentModel.add(FragmentModel(HomeFragment()))
        fragmentModel.add(FragmentModel(SearchFragment()))
        fragmentModel.add(FragmentModel(FeedFragment()))
        fragmentModel.add(FragmentModel(NotificationFragment()))
        fragmentModel.add(FragmentModel(ProfileFragment()))

    }

    private fun initalizeViews() {
        sharedPref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        mainBottomNavigation = findViewById(R.id.main_bottom_navigation)
        mainViewPager = findViewById(R.id.main_view_pager)
        fragmentModel = ArrayList()
    }
}
