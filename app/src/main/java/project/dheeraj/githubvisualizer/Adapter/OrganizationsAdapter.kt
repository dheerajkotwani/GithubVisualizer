package project.dheeraj.githubvisualizer.Adapter

import OrganizationsModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import project.dheeraj.githubvisualizer.R

class OrganizationsAdapter(var context: Context,
                           var organizationsModel: ArrayList<OrganizationsModel>):
    RecyclerView.Adapter<OrganizationsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrganizationsAdapter.ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.layout_org, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return organizationsModel.size
    }


    override fun onBindViewHolder(holder: OrganizationsAdapter.ViewHolder, position: Int) {

        holder.orgName.text = organizationsModel[position].login
        holder.orgUsername.visibility = View.GONE
        holder.orgDesc.text = organizationsModel[position].description

        Glide.with(context)
            .load(organizationsModel[position].avatar_url)
            .into(holder.orgImage)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val orgImage: ImageView = itemView.findViewById(R.id.orgImage)
        val orgName: TextView = itemView.findViewById(R.id.orgName)
        val orgUsername: TextView = itemView.findViewById(R.id.orgUsername)
        val orgDesc: TextView = itemView.findViewById(R.id.orgDesc)


    }


}