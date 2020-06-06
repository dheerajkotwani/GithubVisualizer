package project.dheeraj.githubvisualizer.Adapter

import NotificationModel
import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.javiersc.materialtoast.MaterialToast
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import kotlinx.android.synthetic.main.layout_notifications.view.*
import project.dheeraj.githubvisualizer.Adapter.NotificationsAdapter.*
import project.dheeraj.githubvisualizer.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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


    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notificationTitle.text = notifications[position].subject.title
//        holder.notificationTime.text = notifications[position].updated_at
        if (notifications.get(position).subject.type == "PullRequest") {

            holder.notificationImage.setImageResource(R.drawable.git_pull_request)
            holder.notificationImage.
                setColorFilter(ContextCompat.getColor(context, R.color.green_500),
                    android.graphics.PorterDuff.Mode.SRC_IN)

        }
        else if (notifications.get(position).subject.type == "Issue"){

            holder.notificationImage.setImageResource(R.drawable.issue)
            holder.notificationImage.
                setColorFilter(ContextCompat.getColor(context, R.color.yellow_600),
                    android.graphics.PorterDuff.Mode.SRC_IN)

        }
        holder.itemView.setOnClickListener {

            DynamicToast.makeWarning(context, "Developing").show()

        }

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        try {
            val time: Long = sdf.parse(notifications[position].updated_at).time
            val now = System.currentTimeMillis()
            val ago =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)

            holder.notificationTime.text = ago
        } catch (e: ParseException) {
            e.printStackTrace()
        }


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