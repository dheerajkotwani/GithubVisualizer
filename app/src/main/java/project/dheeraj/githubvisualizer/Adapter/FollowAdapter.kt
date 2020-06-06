package project.dheeraj.githubvisualizer.Adapter

import FollowerModel
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

class FollowAdapter(var context: Context,
                    var searchModel: ArrayList<FollowerModel>):
    RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

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