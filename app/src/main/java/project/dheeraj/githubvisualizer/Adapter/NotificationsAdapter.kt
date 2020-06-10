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

import NotificationModel
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import project.dheeraj.githubvisualizer.Activity.RepositoryInfoActivity
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

    override fun getItemCount() = notifications.size


    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notificationTitle.text = notifications[position].subject.title
        holder.notificationName.text = notifications[position].repository.name
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

            val intent = Intent (context, RepositoryInfoActivity::class.java)
            intent.putExtra("owner", notifications[position].repository.owner.login )
            intent.putExtra("repo", notifications[position].repository.name )
            context.startActivity(intent)

//            DynamicToast.makeWarning(context, "Developing").show()

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
        var notificationName: TextView = itemView.findViewById(R.id.notificationName)

    }


}