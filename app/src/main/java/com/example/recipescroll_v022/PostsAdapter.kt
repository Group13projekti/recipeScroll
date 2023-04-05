package com.example.recipescroll_v022

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipescroll_v022.models.PostDB

class PostsAdapter(val context: Context, private val posts: List<PostDB>)
    : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvRelativeTime: TextView = itemView.findViewById(R.id.tvRelativeTime)
        private val ivPost: ImageView = itemView.findViewById(R.id.ivPost)

        fun bind(post: PostDB) {
            tvUsername.text = post.user?.username
            tvDescription.text = post.description
            Glide.with(context).load(post.imageUrl).into(ivPost)
            tvRelativeTime.text = DateUtils.getRelativeDateTimeString(
                context,
                post.creationTime,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                0
            )
        }
    }
}
