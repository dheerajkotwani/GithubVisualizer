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

package project.dheeraj.githubvisualizer.Fragment.Main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import kotlinx.android.synthetic.main.fragment_home.view.*
import project.dheeraj.githubvisualizer.Activity.*
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.AppConfig.LOGIN
import project.dheeraj.githubvisualizer.AppConfig.NAME
import project.dheeraj.githubvisualizer.AppConfig.SHARED_PREF

import project.dheeraj.githubvisualizer.R

class HomeFragment : Fragment(), FragmentLifecycle {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        sharedPreferences = context!!.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        view.pageDisplayName.text = sharedPreferences.getString(NAME, "User")


        // TODO
        view.cardIssues.setOnClickListener {
//            DynamicToast.makeWarning(context!!, "Developing").show()
            val intent = Intent(context, IssuesActivity::class.java)
            intent.putExtra(AppConfig.LOGIN, sharedPreferences.getString(LOGIN, "User"))
            intent.putExtra("PAGE", "Issues")
            startActivity(intent)
        }

        // TODO
        view.cardPullRequest.setOnClickListener {
//            DynamicToast.makeWarning(context!!, "Developing").show()
            val intent = Intent(context, PullRequestActivity::class.java)
            intent.putExtra(AppConfig.LOGIN, sharedPreferences.getString(LOGIN, "User"))
            intent.putExtra("PAGE", "Pull Requests")
            startActivity(intent)
        }

        view.cardRepo.setOnClickListener {

//            DynamicToast.makeWarning(context!!, "Developing").show()
            val intent = Intent(context, RepositoriesActivity::class.java)
            intent.putExtra(AppConfig.LOGIN, sharedPreferences.getString(LOGIN, "User"))
            intent.putExtra("USER_TYPE", "me")
            startActivity(intent)

        }

        view.cardOrganizations.setOnClickListener {
            startActivity(Intent(context, OrganizationsActivity::class.java))
//            DynamicToast.makeWarning(context!!, "Developing").show()
        }

        view.appInfoIcon.setOnClickListener {
            context!!.startActivity(Intent(context, DeveloperInfoActivity::class.java))
        }

        return view

    }

    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
    }


}
