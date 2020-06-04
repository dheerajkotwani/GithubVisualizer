package project.dheeraj.githubvisualizer.Adapter

import NotificationModel
import SearchModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_notifications.view.*
import project.dheeraj.githubvisualizer.Adapter.NotificationsAdapter.*
import project.dheeraj.githubvisualizer.R

class SearchAdapter(var context: Context,
                    var searchModel: ArrayList<SearchModel>):
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
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}