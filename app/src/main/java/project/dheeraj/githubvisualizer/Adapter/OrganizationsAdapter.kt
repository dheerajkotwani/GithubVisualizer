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

import OrganizationsModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import project.dheeraj.githubvisualizer.R

class OrganizationsAdapter(var context: Context,
                           var organizationsModel: ArrayList<OrganizationsModel>):
    RecyclerView.Adapter<OrganizationsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrganizationsAdapter.ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.layout_org, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return organizationsModel.size
    }


    override fun onBindViewHolder(holder: OrganizationsAdapter.ViewHolder, position: Int) {

        holder.orgName.text = organizationsModel[position].login
        holder.orgUsername.visibility = View.GONE
        holder.orgDesc.text = organizationsModel[position].description

        Glide.with(context)
            .load(organizationsModel[position].avatar_url)
            .into(holder.orgImage)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val orgImage: ImageView = itemView.findViewById(R.id.orgImage)
        val orgName: TextView = itemView.findViewById(R.id.orgName)
        val orgUsername: TextView = itemView.findViewById(R.id.orgUsername)
        val orgDesc: TextView = itemView.findViewById(R.id.orgDesc)


    }


}