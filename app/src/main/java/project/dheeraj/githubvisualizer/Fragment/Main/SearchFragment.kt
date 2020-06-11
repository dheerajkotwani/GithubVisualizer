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

import Items
import SearchModel
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_search.*
import project.dheeraj.githubvisualizer.Adapter.RepositoryAdapter
import project.dheeraj.githubvisualizer.Adapter.SearchAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.Network.GithubApiClient
import project.dheeraj.githubvisualizer.Network.GithubApiInterface

import project.dheeraj.githubvisualizer.R
import project.dheeraj.githubvisualizer.Util.AppUtils
import project.dheeraj.githubvisualizer.ViewModel.ProfileViewModel
import project.dheeraj.githubvisualizer.ViewModel.SearchViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var searchReclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var search: ArrayList<Items>
    private lateinit var progressBar: ProgressBar
    private lateinit var etSearch: EditText
    private lateinit var imageSearch: ImageView
    private lateinit var viewModel: SearchViewModel
    private lateinit var gitSearchProgressbar: ImageView
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_search, container, false)

        sharedPref = context!!.getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        token = "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"
        etSearch = view.findViewById(R.id.search_bar)
        searchReclerView = view.findViewById(R.id.search_recyclerView)
        gitSearchProgressbar = view.findViewById(R.id.gitSearchProgressbar)
        imageSearch = view.findViewById(R.id.search_icon)
        search = ArrayList()

        if (etSearch.text.isNotEmpty())
            getSearchResult()

            Glide.with(this)
                .load(R.drawable.github_loader)
                .into(gitSearchProgressbar)

        imageSearch.setOnClickListener{

            if (!etSearch.text.toString().isNullOrEmpty()) {
                viewModel.getSearchRepo(token, etSearch.text.toString())
                getSearchResult()
                AppUtils.hideSoftKeyBoard(context!!, view)
                if(gitSearchProgressbar.visibility == View.GONE)
                    gitSearchProgressbar.visibility = View.VISIBLE
            }

        }


        etSearch.setOnEditorActionListener { _, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event != null &&
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER){
                if (!etSearch.text.toString().isNullOrEmpty()) {
                    viewModel.getSearchRepo(token, etSearch.text.toString())
                    getSearchResult()
                    AppUtils.hideSoftKeyBoard(context!!, view)
                    if(gitSearchProgressbar.visibility == View.GONE)
                        gitSearchProgressbar.visibility = View.VISIBLE
                }
            }
            true

        }

        return view
    }

    private fun getSearchResult() {

        viewModel.searchUserList.observe(viewLifecycleOwner, Observer {
            if (it.items.isNullOrEmpty()) {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                if (NotificationsProgressBar.visibility == View.VISIBLE)
                    NotificationsProgressBar.visibility = View.GONE
                if(gitSearchProgressbar.visibility == View.VISIBLE)
                    gitSearchProgressbar.visibility = View.GONE
            }
            else {
                searchReclerView.adapter = SearchAdapter(context!!, it.items)
                if(gitSearchProgressbar.visibility == View.VISIBLE)
                    gitSearchProgressbar.visibility = View.GONE
            }
        })

    }
}
