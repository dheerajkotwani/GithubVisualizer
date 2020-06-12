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

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test_repo.*
import project.dheeraj.githubvisualizer.Adapter.ViewPagerAdapter
import project.dheeraj.githubvisualizer.Fragment.Main.*
import project.dheeraj.githubvisualizer.Fragment.Repository.RepoActivityFragment
import project.dheeraj.githubvisualizer.Fragment.Repository.RepoFilesFragment
import project.dheeraj.githubvisualizer.Fragment.Repository.RepoInfoFragment
import project.dheeraj.githubvisualizer.R


class TestRepoActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var owner: String
    private lateinit var repo: String
    private lateinit var fragmentModel: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_repo)
        setSupportActionBar(toolbar)

        if (intent.hasExtra("owner"))
            owner = intent.getStringExtra("owner")
        if (intent.hasExtra("repo")) {
            repo = intent.getStringExtra("repo")
            supportActionBar!!.title = repo
        }

        val bundle = Bundle()
        bundle.putString("owner", owner)
        bundle.putString("repo", repo)
        val repoFilesFragment = RepoFilesFragment()
        val repoInfoFragment = RepoInfoFragment()
        repoFilesFragment.arguments = bundle
        repoInfoFragment.arguments = bundle

        repoTabLayout.addTab(repoTabLayout.newTab().setText("Info"))
        repoTabLayout.addTab(repoTabLayout.newTab().setText("Files"))
        repoTabLayout.addTab(repoTabLayout.newTab().setText("Activity"))

        fragmentModel = ArrayList()
        fragmentModel.add(repoInfoFragment)
        fragmentModel.add(repoFilesFragment)
        fragmentModel.add(FeedsFragment())

        val pagerAdapter = ViewPagerAdapter(supportFragmentManager, fragmentModel)
        repoViewPager.adapter = pagerAdapter
        repoViewPager.currentItem = 0

        repoViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(repoTabLayout))

        repoTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                repoViewPager.currentItem = tab!!.position
            }

        })
    }
}
