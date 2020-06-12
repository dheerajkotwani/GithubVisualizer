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

package project.dheeraj.githubvisualizer.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import project.dheeraj.githubvisualizer.Model.RepositoryModel.RepoDirModel
import project.dheeraj.githubvisualizer.R

class RepositoryDirAdapter(var context: Context,
                           var repoModel: ArrayList<RepoDirModel>,
                           var repoInterface: RepoDirInterface):
    RecyclerView.Adapter<RepositoryDirAdapter.ViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepositoryDirAdapter.ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.layout_repo_dir_name, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount() = repoModel.size


    override fun onBindViewHolder(holder: RepositoryDirAdapter.ViewHolder, position: Int) {

        holder.listName.text = repoModel[position].name

        holder.itemView.setOnClickListener {
            repoInterface.onDirItemClick(position)
        }

    }


    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

        var listName: TextView = itemView.findViewById(R.id.dirName)

    }


}