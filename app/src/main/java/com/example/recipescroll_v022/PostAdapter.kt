package com.example.recipescroll_v022

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//importtaa item_postview
//importtaa firebase post

class PostAdapter (val context: Context,val posts: List <Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = posts.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(post: Post){
            itemView.tvUsername.text = post.user?.username
            itemView.tvDescription.text = post.description
            Glide.with(context).load(post.imageURL).into(itemView.ivPost)
            itemView.tvRelativeTime.text = DateUtils.getRelativeTimeSpanString(post.creationTimeMs)
        }
    }
}