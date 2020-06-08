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

import Items
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import project.dheeraj.githubvisualizer.Activity.ProfileActivity
import project.dheeraj.githubvisualizer.R

class SearchAdapter(var context: Context,
                    var searchModel: ArrayList<Items>):
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.layout_search, parent, false )
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return searchModel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.userName.text = searchModel[position].login
        Glide.with(context)
            .load(searchModel[position].avatar_url)
            .into(holder.userImage)

        var intent = Intent(context,
            ProfileActivity::class.java)
        intent.putExtra("login", searchModel[position].login)
        intent.putExtra("avatar", searchModel[position].avatar_url)

        holder.itemView.setOnClickListener {
            context.startActivity(intent)
        }

    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userImage: ImageView = itemView.findViewById(R.id.user_icon)
        var userName: TextView = itemView.findViewById(R.id.username_text)

    }


}