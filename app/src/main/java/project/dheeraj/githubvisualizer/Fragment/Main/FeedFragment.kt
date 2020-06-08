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
import StarredModel
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_profile.*
import project.dheeraj.githubvisualizer.Adapter.FeedsAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.GithubApiClient
import project.dheeraj.githubvisualizer.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class FeedFragment : Fragment(), FragmentLifecycle {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var feeds: ArrayList<EventsModel>
    private lateinit var feedsRecyclerView: RecyclerView
    private lateinit var feedsAdapter: FeedsAdapter
    private lateinit var feedsProgressBar: ProgressBar
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_feed, container, false)

        sharedPref = context!!.getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        feeds = ArrayList()
        feedsRecyclerView = view.findViewById(R.id.feeds_recyclerView)
        feedsProgressBar = view.findViewById(R.id.feedsProgressBar)

        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);


        feedsAdapter = FeedsAdapter(context!!, feeds)

        page = 1
        feedsRecyclerView. adapter = feedsAdapter
        fetchFeeds(apiInterface, page)

//        feedsRecyclerView.onScrollStateChanged(object: )

        feedsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)
                    && (feedsProgressBar.visibility==View.GONE)) {
//                    Toast.makeText(context, "Last", Toast.LENGTH_LONG).show()
                    page++
                    fetchFeeds(apiInterface, page)
                }
            }
        })





        return view
    }

    private fun fetchFeeds(
        apiInterface: GithubApiInterface,
        page: Int
    ) {

        if (feedsProgressBar.visibility == View.GONE)
            feedsProgressBar.visibility = View.VISIBLE
        var call: Call<ArrayList<EventsModel>> =
            apiInterface.getEvents(
                "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}",
                "${sharedPref.getString(AppConfig.LOGIN, "")}", page
            )
        try {


            call.enqueue(object : Callback<ArrayList<EventsModel>> {
                override fun onFailure(call: Call<ArrayList<EventsModel>>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    feedsProgressBar.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<ArrayList<EventsModel>>,
                    response: Response<ArrayList<EventsModel>>
                ) {
                    try {
                        for (i in response.body()!!) {
                            feeds.add(i)
                            feedsAdapter.notifyItemChanged(feeds.size)
                        }
                        feedsProgressBar.visibility = View.GONE

                    } catch (e: Exception) {
                        Timber.e(e)
                        feedsProgressBar.visibility = View.GONE
                    }
                }
            })
        } catch (e: Exception) {
            Timber.e(e)
            feedsProgressBar.visibility = View.GONE
        }
    }



    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
    }


}
