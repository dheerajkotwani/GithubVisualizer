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

package project.dheeraj.githubvisualizer.Fragment.Issues

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_all_issues.*
import project.dheeraj.githubvisualizer.Adapter.IssuesAdapter
import project.dheeraj.githubvisualizer.AppConfig

import project.dheeraj.githubvisualizer.R
import project.dheeraj.githubvisualizer.ViewModel.IssuesViewModel

class AssignedIssuesFragment : Fragment() {

    private lateinit var viewModel: IssuesViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var token: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_issues, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Glide.with(this)
            .load(R.drawable.github_loader)
            .into(gitProgressbar)

        sharedPref = context!!.getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        token = "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"

        viewModel = ViewModelProviders.of(this).get(IssuesViewModel::class.java)
        observeIssues()
        viewModel.getIssues(token, "assigned")

    }

    private fun observeIssues(){

        viewModel.issuesList.observe(viewLifecycleOwner, Observer {
            if (it.size==0){
                if (gitProgressbar.isVisible)
                    gitProgressbar.visibility = View.GONE
                if (!noIssueFound.isVisible)
                    noIssueFound.visibility = View.VISIBLE
            }
            else {
                issuesRecyclerView.adapter = IssuesAdapter (context!!, it)
                if (gitProgressbar.isVisible)
                    gitProgressbar.visibility = View.GONE
            }
        })

    }
}
