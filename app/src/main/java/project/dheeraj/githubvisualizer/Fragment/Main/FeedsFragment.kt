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

import EventsModel
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_follow.*
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_notification.*
import project.dheeraj.githubvisualizer.Adapter.FeedsAdapter
import project.dheeraj.githubvisualizer.AppConfig

import project.dheeraj.githubvisualizer.R
import project.dheeraj.githubvisualizer.ViewModel.FeedsViewModel

class FeedsFragment : Fragment() {

    companion object {
        fun newInstance() = FeedsFragment()
    }

    private lateinit var viewModel: FeedsViewModel
    private lateinit var sharedPref: SharedPreferences
    private var page: Int = 1
    private var pageDone: Int = 0
    private lateinit var feedsList: ArrayList<EventsModel>
    private lateinit var adapter: FeedsAdapter
    private lateinit var token: String
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPref = context!!.getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        token = "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"
        username = "${sharedPref.getString(AppConfig.LOGIN, "")}"

        return inflater.inflate(R.layout.fragment_feed, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FeedsViewModel::class.java)

        feedsList = ArrayList()
        page = 0
        viewModel.getFeeds(token, username, page)

        Glide.with(this)
            .load(R.drawable.github_loader)
            .into(gitFeedsProgressbar)
        adapter = FeedsAdapter(context!!, feedsList)

        feedsRecyclerView.adapter = adapter

        viewModel.feedsList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {

                Toast.makeText(context, "No more items", Toast.LENGTH_SHORT).show()
                if (feedsProgressBar.isRefreshing)
                    feedsProgressBar.isRefreshing = false
                if(gitFeedsProgressbar.visibility == View.VISIBLE)
                    gitFeedsProgressbar.visibility = View.GONE
                buttonFeedsLoadMore.visibility = View.GONE
            }
            else {

//                feedsRecyclerView.adapter = FeedsAdapter(context!!, feeds)
                feedsList.addAll(it)
                adapter.notifyDataSetChanged()

                buttonFeedsLoadMore.isClickable = true
                buttonFeedsLoadMore.visibility = View.VISIBLE

                if (feedsProgressBar.isRefreshing)
                    feedsProgressBar.isRefreshing = false
                if(gitFeedsProgressbar.visibility == View.VISIBLE)
                    gitFeedsProgressbar.visibility = View.GONE
            }
        })

        feedsProgressBar.setOnRefreshListener {

            viewModel.getFeeds(token, username, page)

        }

        buttonFeedsLoadMore.setOnClickListener {

            page++
            buttonFeedsLoadMore.isClickable = false

            if (!feedsProgressBar.isRefreshing)
                feedsProgressBar.isRefreshing = true

            viewModel.getFeeds(token, username, page)

        }

    }

    override fun onResume() {
        super.onResume()
    }

}
