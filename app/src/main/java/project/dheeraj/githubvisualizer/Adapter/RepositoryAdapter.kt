package project.dheeraj.githubvisualizer.Adapter

import EventsModel
import NotificationModel
import OrganizationsModel
import RepositoryModel
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

class RepositoryAdapter(var context: Context,
                        var repoModel: ArrayList<RepositoryModel>):
    RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepositoryAdapter.ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.layout_repo, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return repoModel.size
    }


    override fun onBindViewHolder(holder: RepositoryAdapter.ViewHolder, position: Int) {
        holder.title.text = repoModel[position].name
        holder.description.text = repoModel[position].name
        holder.language.text = repoModel[position].language
        holder.stars.text = repoModel[position].stargazers_count.toString()
        holder.forks.text = repoModel[position].forks_count.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title:TextView = itemView.findViewById(R.id.repo_name)
        var description:TextView = itemView.findViewById(R.id.repo_description)
        var language:TextView = itemView.findViewById(R.id.repo_language)
        var stars:TextView = itemView.findViewById(R.id.repo_stars)
        var forks:TextView = itemView.findViewById(R.id.repo_forks)

    }


}