package project.dheeraj.githubvisualizer.Adapter

import NotificationModel
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

class NotificationsAdapter(var context: Context,
                           var notifications: ArrayList<NotificationModel>):
    RecyclerView.Adapter<ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.layout_notifications, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notifications.size
//        return 5
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notificationTitle.text = notifications[position].subject.title
        holder.notificationTime.text = notifications[position].updated_at
        if (notifications.get(position).subject.type == "PullRequest")
            holder.notificationImage.setImageResource(R.drawable.git_pull_request)
        else if (notifications.get(position).subject.type == "Issue")
            holder.notificationImage.setImageResource(R.drawable.issue)


        // Test data
//        holder.notificationTitle.text = "hey"
//        holder.notificationTime.text = "notifications[position].updated_at"0-12
//        if (notifications.get(position).subject.type == "PullRequest")
//            holder.notificationImage.setImageResource(R.drawable.git_pull_request)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var notificationImage: ImageView = itemView.findViewById(R.id.notification_icon)
        var notificationTitle: TextView = itemView.findViewById(R.id.notification_text)
        var notificationTime: TextView = itemView.findViewById(R.id.notification_time)

    }


}