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

package project.dheeraj.githubvisualizer.Fragment.Repository

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_repo_files.*
import project.dheeraj.githubvisualizer.Activity.CodeViewerActivity
import project.dheeraj.githubvisualizer.Adapter.RepoDirInterface
import project.dheeraj.githubvisualizer.Adapter.RepositoryContentAdapter
import project.dheeraj.githubvisualizer.Adapter.RepositoryDirAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.Model.RepositoryModel.RepoDirModel
import project.dheeraj.githubvisualizer.Model.RepositoryModel.RepositoryContentModel
import project.dheeraj.githubvisualizer.R
import project.dheeraj.githubvisualizer.ViewModel.RepositoryViewModel
import java.util.*
import java.util.Collections.sort
import kotlin.Comparator
import kotlin.collections.ArrayList


class RepoFilesFragment : Fragment(), RepoDirInterface {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var token: String
    private lateinit var username: String
    private lateinit var repo: String
    private lateinit var viewModel: RepositoryViewModel
    private lateinit var repoDirList: ArrayList<RepoDirModel>
    private lateinit var repoDirFiles: ArrayList<RepositoryContentModel>
    private lateinit var adapter: RepositoryContentAdapter
    private lateinit var adapterDir: RepositoryDirAdapter
    private lateinit var path: String
    private var canClick: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repo_files, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedPref = context!!.getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel::class.java)

        repoDirList = ArrayList()
        repoDirFiles = ArrayList()

        adapterDir = RepositoryDirAdapter(context!!, repoDirList, this)
        adapter = RepositoryContentAdapter(context!!, repoDirFiles, this)

//        repoFilesRecyclerView.adapter = adapter
        repoDirRecyclerView.adapter = adapterDir

        token = "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}"
        username = arguments!!.getString("owner", "")
        repo = arguments!!.getString("repo", "")
        path=""

        observeFiles()
        viewModel.repoContent(token, username, repo, path)

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener(object: View.OnKeyListener{
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {

                if (p1 == KeyEvent.KEYCODE_BACK && p2!!.action == KeyEvent.ACTION_UP) {

                    if (repoDirList.size == 1) {
                        return false
                    }
                    else {
                        repoDirFiles = repoDirList[repoDirList.size-2].content
                        repoDirList.removeAt(repoDirList.size-1)
                        repoFilesRecyclerView.adapter = RepositoryContentAdapter(context!!, repoDirFiles, this@RepoFilesFragment)
                        adapterDir.notifyDataSetChanged()
                        return true
                    }
                }
                return false
            }
        })


    }

    private fun observeFiles() {

        viewModel.repoContent.observe(viewLifecycleOwner, Observer {

            canClick = true
            repoDirFiles = it
            repoDirFiles.sortBy { contentModel ->
                contentModel.type
            }
            if (repoDirList.size == 0)
               repoDirList.add(RepoDirModel(".", "", repoDirFiles))
            else {
                repoDirList.add(RepoDirModel("${path.substringAfterLast("/")}", path, repoDirFiles))
                adapterDir.notifyDataSetChanged()
                repoDirRecyclerView.layoutManager!!.scrollToPosition(repoDirList.size-1)
            }
            adapterDir.notifyDataSetChanged()
//            adapter.notifyDataSetChanged()

            repoFilesRecyclerView.adapter = RepositoryContentAdapter(context!!, repoDirFiles, this)

        })

    }

    override fun onItemLongClick(position: Int) {

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${repoDirFiles[position].html_url}\n\nShared via *Github Visualizer App*\n " +
                    "https://play.google.com/store/apps/details?id=project.dheeraj.githubvisualizer")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)

    }

    override fun onFileItemClick(position: Int) {
        if (canClick) {
            if (repoDirFiles[position].type == "dir") {

                path = "${repoDirFiles[position].path}"
                viewModel.repoContent(token, username, repo, path)
                canClick = false

            }
            else {

                var intent = Intent(context!!, CodeViewerActivity::class.java)
                intent.putExtra("url", repoDirFiles[position].download_url)
                intent.putExtra("html_url", repoDirFiles[position].html_url)
                startActivity(intent)

            }
        }
    }


    override fun onDirItemClick(position: Int) {

        repoDirFiles = repoDirList[position].content
        repoFilesRecyclerView.adapter = RepositoryContentAdapter(context!!, repoDirFiles, this)
        repoDirList.subList(position+1, repoDirList.size).clear()
        adapterDir.notifyDataSetChanged()

    }





}
