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

import RepositoryModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import de.hdodenhof.circleimageview.CircleImageView
import project.dheeraj.githubvisualizer.Activity.RepositoryInfoActivity
import project.dheeraj.githubvisualizer.Model.RepositoryModel.RepositoryContentModel
import project.dheeraj.githubvisualizer.R

class RepositoryContentAdapter(var context: Context,
                               var repoModel: ArrayList<RepositoryContentModel>,
                               var repoInterface: RepoDirInterface):
    RecyclerView.Adapter<RepositoryContentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepositoryContentAdapter.ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.layout_file_item, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount() = repoModel.size


    override fun onBindViewHolder(holder: RepositoryContentAdapter.ViewHolder, position: Int) {

        holder.listName.text = repoModel[position].name
        if (repoModel[position].type == "dir") {
            Glide.with(context)
                .load(R.drawable.ic_folder_black_24dp)
                .into(holder.listIcon)
        }
        else {
            Glide.with(context)
                .load(R.drawable.ic_file_list)
                .into(holder.listIcon)
        }

        holder.itemView.setOnClickListener {
            repoInterface.onFileItemClick(position)
        }


    }


    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

        var listName: TextView = itemView.findViewById(R.id.listName)
        var listDate: TextView = itemView.findViewById(R.id.listDate)
        var listIcon: ImageView = itemView.findViewById(R.id.listIcon)

    }

}