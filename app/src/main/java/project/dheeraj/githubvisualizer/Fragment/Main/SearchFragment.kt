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
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_search.*
import project.dheeraj.githubvisualizer.Adapter.SearchRepoAdapter
import project.dheeraj.githubvisualizer.Adapter.SearchUserAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.Model.SearchModel.Item

import project.dheeraj.githubvisualizer.R
import project.dheeraj.githubvisualizer.Util.AppUtils
import project.dheeraj.githubvisualizer.ViewModel.SearchViewModel

class SearchFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var searchReclerView: RecyclerView
    private lateinit var searchUserAdapter: SearchUserAdapter
    private lateinit var searchUser: ArrayList<Items>
    private lateinit var searchRepo: ArrayList<Item>
    private lateinit var viewModel: SearchViewModel
    private lateinit var token: String
    private var fromUser: Boolean = false
    private var fromRepo: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        sharedPref = context!!.getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        token = "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"
        searchUser = ArrayList()
        searchRepo = ArrayList()

        getSearchResult()

        if (etSearch.text.isNotEmpty())
            getSearchResult()

        Glide.with(this)
            .load(R.drawable.github_loader)
            .into(gitSearchProgressbar)

        imageSearch.setOnClickListener{

            if (!etSearch.text.toString().isNullOrEmpty()) {
                fromUser = false
                fromRepo = false
                viewModel.getSearchUserSmall(token, etSearch.text.toString())
                viewModel.getSearchRepoSmall(token, etSearch.text.toString())
                AppUtils.hideSoftKeyBoard(context!!, view!!)
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
                    fromUser = false
                    fromRepo = false
                    viewModel.getSearchUserSmall(token, etSearch.text.toString())
                    viewModel.getSearchRepoSmall(token, etSearch.text.toString())
                    AppUtils.hideSoftKeyBoard(context!!, view!!)
                    if(gitSearchProgressbar.visibility == View.GONE)
                        gitSearchProgressbar.visibility = View.VISIBLE
                }
            }
            true

        }


        tvShowAllUser.setOnClickListener {
            if(gitSearchProgressbar.visibility == View.GONE)
                gitSearchProgressbar.visibility = View.VISIBLE
            viewModel.getSearchUser(token, etSearch.text.toString())
            fromUser = true
            fromRepo = false

        }

        tvShowAllRepo.setOnClickListener {

            if(gitSearchProgressbar.visibility == View.GONE)
                gitSearchProgressbar.visibility = View.VISIBLE
            viewModel.getSearchRepo(token, etSearch.text.toString())
            fromUser = false
            fromRepo = true
        }


    }


    private fun getSearchResult() {

        viewModel.searchUserList.observe(viewLifecycleOwner, Observer {
            if (it.items.isNullOrEmpty()) {
//                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                if (gitSearchProgressbar.visibility == View.VISIBLE)
                    gitSearchProgressbar.visibility = View.GONE
                if(gitSearchProgressbar.visibility == View.VISIBLE)
                    gitSearchProgressbar.visibility = View.GONE
            }
            else {

                if (categorySearch.isVisible && fromUser) {
                    categorySearch.visibility = View.GONE
                    allSearchRecyclerView.visibility = View.VISIBLE
                    allSearchRecyclerView.adapter = SearchUserAdapter(context!!, it.items)
                    allSearchRecyclerView.scrollToPosition(0)
                }
                else if (!fromRepo && !fromUser)  {
                    if (!categorySearch.isVisible)
                        categorySearch.visibility = View.VISIBLE
                    userSearchRecyclerView.adapter = SearchUserAdapter(context!!, it.items)
                    if(allSearchRecyclerView.isVisible)
                        allSearchRecyclerView.visibility = View.GONE
                }

                if(gitSearchProgressbar.visibility == View.VISIBLE)
                    gitSearchProgressbar.visibility = View.GONE
            }
        })


        viewModel.searchRepoList.observe(viewLifecycleOwner, Observer {
            if (it.items.isNullOrEmpty()) {
//                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                if (gitSearchProgressbar.visibility == View.VISIBLE)
                    gitSearchProgressbar.visibility = View.GONE
                if(gitSearchProgressbar.visibility == View.VISIBLE)
                    gitSearchProgressbar.visibility = View.GONE
            }
            else {

                if (categorySearch.isVisible && fromRepo) {
                    categorySearch.visibility = View.GONE
                    allSearchRecyclerView.visibility = View.VISIBLE
                    allSearchRecyclerView.adapter = SearchRepoAdapter(context!!, it.items)
                    allSearchRecyclerView.scrollToPosition(0)
                }
                else if (!fromRepo && !fromUser) {
                    if (!categorySearch.isVisible)
                        categorySearch.visibility = View.VISIBLE
                    repoSearchRecyclerView.adapter = SearchRepoAdapter(context!!, it.items)
                    if(allSearchRecyclerView.isVisible)
                        allSearchRecyclerView.visibility = View.GONE
                }

                if(gitSearchProgressbar.visibility == View.VISIBLE)
                    gitSearchProgressbar.visibility = View.GONE
            }
        })

    }
}
