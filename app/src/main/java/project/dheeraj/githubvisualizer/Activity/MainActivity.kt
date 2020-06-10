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

import GithubUserModel
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import project.dheeraj.githubvisualizer.*
import project.dheeraj.githubvisualizer.Adapter.ViewPagerAdapter
import project.dheeraj.githubvisualizer.AppConfig.ACCESS_TOKEN
import project.dheeraj.githubvisualizer.AppConfig.SHARED_PREF
import project.dheeraj.githubvisualizer.Fragment.Main.*
import project.dheeraj.githubvisualizer.Network.GithubApiClient
import project.dheeraj.githubvisualizer.Network.GithubApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var apiInterface: GithubApiInterface
    private lateinit var sharedPref: SharedPreferences
    private lateinit var mainBottomNavigation: MeowBottomNavigation
    private lateinit var mainViewPager: ViewPager
    private lateinit var fragmentModel: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()

        setBottomNavigation()

        setViewPager()

        mainViewPager.
            setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    mainBottomNavigation.show(position+1)
                }

            })

        mainBottomNavigation.setOnShowListener {
            // YOUR CODES
        }

        mainBottomNavigation.setOnClickMenuListener {
            // YOUR CODES
            mainViewPager.currentItem = it.id-1

        }

        apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);
        var call: Call<GithubUserModel> =
            apiInterface.getUserData("token ${sharedPref.getString(ACCESS_TOKEN, "")}")
        call.enqueue(object : Callback<GithubUserModel> {
            override fun onFailure(call: Call<GithubUserModel>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<GithubUserModel>, response: Response<GithubUserModel>) {
                
                sharedPref.edit()
                    .putString(AppConfig.NAME, response.body()!!.name)
                    .putString(AppConfig.LOGIN, response.body()!!.login)
                    .apply()
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

        fragmentModel.add(HomeFragment())
        fragmentModel.add(SearchFragment())
        fragmentModel.add(FeedsFragment())
        fragmentModel.add(NotificationsFragment())
        fragmentModel.add(ProfileFragment())

    }

    private fun initializeViews() {
        sharedPref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        mainBottomNavigation = findViewById(R.id.mainBottomNavigation)
        mainViewPager = findViewById(R.id.mainViewPager)
        fragmentModel = ArrayList()

        FirebaseApp.initializeApp(baseContext)
        FirebaseAnalytics.getInstance(baseContext)
        FirebaseMessaging.getInstance().isAutoInitEnabled

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
