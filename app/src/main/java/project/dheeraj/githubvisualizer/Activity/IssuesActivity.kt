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

import IssuesModel
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_issues.*
import project.dheeraj.githubvisualizer.Adapter.IssuesAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.GithubApiClient
import project.dheeraj.githubvisualizer.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class IssuesActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var issues: ArrayList<IssuesModel>
    private lateinit var issuesAdapter: IssuesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issues)

        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);

        issues = ArrayList()
        issuesAdapter = IssuesAdapter(this, issues)

        if (intent.hasExtra("PAGE"))
            if (intent.getStringExtra("PAGE") == "Issues")
                fetchIssues(apiInterface)
            else
                fetchPulls(apiInterface)
        else
            fetchIssues(apiInterface)

        buttonBack.setOnClickListener {
            onBackPressed()
        }


    }

    private fun fetchIssues(apiInterface: GithubApiInterface) {

        pageTitle.text = "Issues"

        issueProgressbar.visibility = View.VISIBLE

        var call: Call<ArrayList<IssuesModel>> =
            apiInterface.getIssues(
                "token ${sharedPref.getString(
                    AppConfig.ACCESS_TOKEN,
                    ""
                )}"
            )
        try {
            call.enqueue(object : Callback<ArrayList<IssuesModel>> {
                override fun onFailure(call: Call<ArrayList<IssuesModel>>, t: Throwable) {
                    Toast.makeText(
                        this@IssuesActivity,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    issueProgressbar.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<ArrayList<IssuesModel>>,
                    response: Response<ArrayList<IssuesModel>>
                ) {
                    try {

                        for (i in response.body()!!) {
                            try {
                                val d = i.pull_request.url
                            }
                            catch (e: Exception) {
                                issues.add(i)
                            }
                        }
                        if (response.body()!!.size == 0)
                            noIssueFound.visibility = View.VISIBLE
                        issuesRecyclerView.adapter = issuesAdapter
                        issuesAdapter.notifyDataSetChanged()
                        issueProgressbar.visibility = View.GONE
                    } catch (e: Exception) {
                        Timber.e(e)
                        issueProgressbar.visibility = View.GONE
                        noIssueFound.visibility = View.VISIBLE
                    }

                }
            })
        } catch (e: Exception) {
            Timber.e(e)
            issueProgressbar.visibility = View.GONE
            noIssueFound.visibility = View.VISIBLE
        }
    }

private fun fetchPulls(apiInterface: GithubApiInterface) {

    pageTitle.text = "Pull Request"

        issueProgressbar.visibility = View.VISIBLE

        var call: Call<ArrayList<IssuesModel>> =
            apiInterface.getIssues(
                "token ${sharedPref.getString(
                    AppConfig.ACCESS_TOKEN,
                    ""
                )}"
            )
        try {
            call.enqueue(object : Callback<ArrayList<IssuesModel>> {
                override fun onFailure(call: Call<ArrayList<IssuesModel>>, t: Throwable) {
                    Toast.makeText(
                        this@IssuesActivity,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    issueProgressbar.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<ArrayList<IssuesModel>>,
                    response: Response<ArrayList<IssuesModel>>
                ) {
                    try {

                        for (i in response.body()!!) {
                            try {
                                val d = i.pull_request.url
                            }
                            catch (e: Exception) {
                                Timber.e(e)
                            }
                            finally {
                                issues.add(i)
                            }
                        }
                        if (response.body()!!.size == 0)
                            noIssueFound.visibility = View.VISIBLE
                        issuesRecyclerView.adapter = issuesAdapter
                        issuesAdapter.notifyDataSetChanged()
                        issueProgressbar.visibility = View.GONE
                    } catch (e: Exception) {
                        Timber.e(e)
                        issueProgressbar.visibility = View.GONE
                        noIssueFound.visibility = View.VISIBLE
                    }

                }
            })
        } catch (e: Exception) {
            Timber.e(e)
            issueProgressbar.visibility = View.GONE
            noIssueFound.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}


