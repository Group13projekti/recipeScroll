package com.example.recipescroll_v022

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipescroll_v022.models.PostDB

class PostAdapter(val context: FrontPage, val posts: List <PostDB>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = posts.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currItem = posts[position]
        holder.username.text = currItem.user?.uname
        Glide.with(context).load(currItem.imageUrl).into(holder.Post)
        Glide.with(context).load(currItem.user?.profileImageUrl).into(holder.profileImage)
        holder.relativetime.text = DateUtils.getRelativeTimeSpanString(currItem.creationTime)
        holder.description.text = currItem.description

        if (currItem.user?.profileImageUrl != null) {
            Glide.with(context)
                .load(currItem.user!!.profileImageUrl)
                .placeholder(R.drawable.placeholder_image) // asetetaan profiilikuvan sijaan placeholder-image
                .into(holder.profileImage)
        } else {
            Glide.with(context)
                .load(R.drawable.placeholder_image) // asetetaan placeholder-image, jos profiilikuvaa ei ole
                .into(holder.profileImage)
        }
    }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        val username : TextView = itemView.findViewById(R.id.tvUsername)
        val relativetime : TextView = itemView.findViewById(R.id.tvRelativetime)
        val Post : ImageView = itemView.findViewById(R.id.ivPost)
        val profileImage : ImageView = itemView.findViewById(R.id.profile_image)
        val description : TextView = itemView.findViewById(R.id.tvDescription)



    }
}