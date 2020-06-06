package project.dheeraj.githubvisualizer.Adapter

import EventsModel
import NotificationModel
import OrganizationsModel
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

class OrganizationsAdapter(var context: Context,
                           var organizationsModel: ArrayList<OrganizationsModel>):
    RecyclerView.Adapter<OrganizationsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrganizationsAdapter.ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.layout_feeds, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return organizationsModel.size
    }


    override fun onBindViewHolder(holder: OrganizationsAdapter.ViewHolder, position: Int) {
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


}