package project.dheeraj.githubvisualizer.Adapter

import RepositoryModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import de.hdodenhof.circleimageview.CircleImageView
import project.dheeraj.githubvisualizer.Activity.RepositoryInfoActivity
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
        if (repoModel[position].description.isNullOrEmpty())
            holder.description.text = repoModel[position].name
        else
            holder.description.text = repoModel[position].description
        holder.language.text = repoModel[position].language
        holder.stars.text = repoModel[position].stargazers_count.toString()
        holder.forks.text = repoModel[position].forks_count.toString()
        holder.repoOwner.text = repoModel[position].owner.login

        Glide.with(context)
            .load(repoModel[position].owner.avatar_url)
            .into(holder.repoImage)

        holder.itemView.setOnClickListener {

            val intent = Intent (context, RepositoryInfoActivity::class.java)
            intent.putExtra("owner", repoModel[position].owner.login )
            intent.putExtra("repo", repoModel[position].name )
            context.startActivity(Intent(intent))

//            DynamicToast.makeWarning(context, "Developing").show()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title:TextView = itemView.findViewById(R.id.repo_name)
        var description:TextView = itemView.findViewById(R.id.repo_description)
        var language:TextView = itemView.findViewById(R.id.repo_language)
        var stars:TextView = itemView.findViewById(R.id.repo_stars)
        var forks:TextView = itemView.findViewById(R.id.repo_forks)
        var repoOwner:TextView = itemView.findViewById(R.id.repoOwner)
        var repoImage:CircleImageView = itemView.findViewById(R.id.repoImage)


    }


}