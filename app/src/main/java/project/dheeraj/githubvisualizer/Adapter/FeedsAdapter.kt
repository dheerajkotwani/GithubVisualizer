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

import EventsModel
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.icu.lang.UProperty.INT_START
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import de.hdodenhof.circleimageview.CircleImageView
import project.dheeraj.githubvisualizer.Activity.ProfileActivity
import project.dheeraj.githubvisualizer.Activity.RepositoryInfoActivity
import project.dheeraj.githubvisualizer.R
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class FeedsAdapter(var context: Context,
                   var eventsModel: ArrayList<EventsModel>):
    RecyclerView.Adapter<FeedsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedsAdapter.ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.layout_feeds, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount() = eventsModel.size


    override fun onBindViewHolder(holder: FeedsAdapter.ViewHolder, position: Int) {

        Glide.with(context)
            .load(eventsModel[position].actor.avatar_url)
            .into(holder.feedUserImage)

        holder.feedUserName.text = eventsModel[position].actor.login

        when (eventsModel[position].type){
            "WatchEvent" -> {
                val s = SpannableStringBuilder()
                    .append("Starred ")
                    .bold { append(eventsModel[position].repo.name) }

                holder.feedUserTitle.text = s
                holder.feedUserDescription.visibility = View.GONE
            }
            "IssueCommentEvent" -> {
                val s = SpannableStringBuilder()
                    .append("${eventsModel[position].payload.action.capitalize()}" +
                            " comment on issue " +
                            "${eventsModel[position].payload.issue.number} in ")
                    .bold { append(eventsModel[position].repo.name) }

                holder.feedUserTitle.text = s
                holder.feedUserDescription.text = eventsModel[position].payload.comment.body
            }
            "PullRequestReviewCommentEvent" -> {

                val s = SpannableStringBuilder()
                    .append("${eventsModel[position].payload.action.capitalize()}" +
                            " pull request review comment at ")
                    .bold { append(eventsModel[position].repo.name) }

                holder.feedUserTitle.text = s
                holder.feedUserDescription.text = eventsModel[position].payload.comment.body

            }
            "CreateEvent" -> {

                val s = SpannableStringBuilder()
                    .append("Created ${eventsModel[position].payload.ref_type} " )
                    .bold { append(eventsModel[position].repo.name) }

                holder.feedUserTitle.text = s
                holder.feedUserDescription.text = eventsModel[position].payload.description

            }
            "PullRequestEvent" -> {

                val s = SpannableStringBuilder()
                    .append("${eventsModel[position].payload.action.capitalize()} pull request " +
                            "${eventsModel[position].payload.pull_request.number} in " )
                    .bold { append(eventsModel[position].repo.name) }

                holder.feedUserTitle.text = s
                holder.feedUserDescription.text = "${eventsModel[position].payload.pull_request.title}\n" +
                        "${eventsModel[position].payload.pull_request.body}"

            }
            "PushEvent" -> {

                val s = SpannableStringBuilder()
                    .append("Pushed to " +
                            "${eventsModel[position].payload.ref.substringAfterLast("/")} in " )
                    .bold { append(eventsModel[position].repo.name) }

                holder.feedUserTitle.text = s

                val d = SpannableStringBuilder()

                for (i in eventsModel[position].payload.commits) {
                    d.color(Color.BLUE) { append("\n${i.sha.substring(0,7)} ") }
                        .append("${i.message}")
                }

                holder.feedUserDescription.text = d

            }
            "DeleteEvent" -> {

                val s = SpannableStringBuilder()
                    .append("Delete ${eventsModel[position].payload.ref_type} ")
                    .bold { append(eventsModel[position].payload.ref) }

                holder.feedUserTitle.text = s
                holder.feedUserDescription.visibility = View.GONE

            }
            "ForkEvent" -> {

                val s = SpannableStringBuilder()
                    .append("Forked ")
                    .bold { append("${eventsModel[position].repo.name} ")}
                    .append("to ")
                    .bold { append(eventsModel[position].payload.forkee.full_name) }

                holder.feedUserTitle.text = s
                holder.feedUserDescription.visibility = View.GONE

            }
            "IssuesEvent" -> {

                val s = SpannableStringBuilder()
                    .append("${eventsModel[position].payload.action.capitalize()} issue " +
                            "${eventsModel[position].payload.issue.number} in " )
                    .bold { append(eventsModel[position].repo.name) }

                holder.feedUserTitle.text = s
                holder.feedUserDescription.text = "${eventsModel[position].payload.issue.title}\n" +
                        "${eventsModel[position].payload.issue.body}"

            }
            "PublicEvent" -> {

                val s = SpannableStringBuilder()
                    .append("Made ")
                    .bold { append(eventsModel[position].repo.name) }
                    .append(" public.")

                holder.feedUserTitle.text = s
                holder.feedUserDescription.visibility = View.GONE

            }
            "MemberEvent" -> {

                val s = SpannableStringBuilder()
                    .append("Added Member ")
                    .bold { append(eventsModel[position].payload.member.login) }
                    .append(" to ")
                    .bold { append(eventsModel[position].repo.name) }

                holder.feedUserTitle.text = s
                holder.feedUserDescription.visibility = View.GONE

            }
            "ReleaseEvent" -> {

                try {

                    val s = SpannableStringBuilder()
                    if (eventsModel[position].payload.release.tag_name != null) {
                            s.append(
                                "${eventsModel[position].payload.action.capitalize()} release " +
                                        "${eventsModel[position].payload.release.tag_name} at "
                            )
                            .bold { append(eventsModel[position].repo.name) }
                    }
                    else if (eventsModel[position].payload.release.name != null){
                        s.append(
                            "${eventsModel[position].payload.action.capitalize()} release " +
                                    "${eventsModel[position].payload.release.name} at "
                        )
                            .bold { append(eventsModel[position].repo.name) }
                    }
                    else {
                        s.append(
                            "${eventsModel[position].payload.action.capitalize()} release at ")
                            .bold { append(eventsModel[position].repo.name) }
                    }
                    holder.feedUserTitle.text = s
                }
                catch (e: Exception) {
                    Timber.e(e)
                    val s = SpannableStringBuilder()
                        .append("${eventsModel[position].payload.action.capitalize()} at")
                        .bold { append(eventsModel[position].repo.name) }

                    holder.feedUserTitle.text = s
                }
                holder.feedUserDescription.visibility = View.GONE

            }
            else -> {

                val s = SpannableStringBuilder()
                    .bold { color(Color.RED) {append("Type ${eventsModel[position].type}") }}

                holder.feedUserTitle.text = s
                holder.feedUserDescription.visibility = View.GONE
            }
        }

        holder.feedUserName.setOnClickListener {
            var intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("login", eventsModel[position].actor.login)
            intent.putExtra("avatar", eventsModel[position].actor.avatar_url)
            context.startActivity(intent)
        }

        holder.feedUserImage.setOnClickListener {
            var intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("login", eventsModel[position].actor.login)
            intent.putExtra("avatar", eventsModel[position].actor.avatar_url)
            context.startActivity(intent)
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        try {
            val time: Long = sdf.parse(eventsModel[position].created_at).time
            val now = System.currentTimeMillis()
            val ago =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)

            holder.feedUserTime.text = ago
        } catch (e: ParseException) {
            e.printStackTrace()
        }


        holder.itemView.setOnClickListener {

            val intent = Intent (context, RepositoryInfoActivity::class.java)
            intent.putExtra("repo", eventsModel[position].repo.name.substringAfterLast("/") )
            intent.putExtra("owner", eventsModel[position].repo.name.substringBeforeLast("/") )
            context.startActivity(Intent(intent))

//            DynamicToast.makeWarning(context, "Developing").show()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var feedUserImage = itemView.findViewById<CircleImageView>(R.id.feedUserImage)
        var feedUserName = itemView.findViewById<TextView>(R.id.feedUserName)
        var feedUserTime = itemView.findViewById<TextView>(R.id.feedTime)
        var feedUserTitle = itemView.findViewById<TextView>(R.id.feedTitle)
        var feedUserDescription = itemView.findViewById<TextView>(R.id.feedDescription)

    }


}