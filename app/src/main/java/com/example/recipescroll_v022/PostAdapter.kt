package com.example.recipescroll_v022

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipescroll_v022.models.PostDB
import com.example.recipescroll_v022.models.UserDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class PostAdapter(val context: FrontPage, val posts: List<PostDB>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val favoriteStates = mutableMapOf<String, Boolean>()   //luodaan faovriteille statet

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)

        val favButton = view.findViewById<CheckBox>(R.id.cb_favorites)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userId!!)
        var favorites = emptyList<String>()

        return ViewHolder(view, favButton, userDocRef, favorites)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currItem = posts[position]
        holder.username.text = currItem.user?.uname
        Glide.with(context).load(currItem.imageUrl).into(holder.Post)
        Glide.with(context).load(currItem.user?.profileImageUrl).into(holder.profileImage)
        holder.relativetime.text = DateUtils.getRelativeTimeSpanString(currItem.creationTime.toLong())
        holder.description.text = currItem.description

        val isFavorite = favoriteStates[currItem.postId] ?: false
        holder.favButton.isChecked = isFavorite


        holder.userDocRef.get().addOnCompleteListener { task ->             //tarkistaa onko postaus favoriteissa ja laittaa favorite napin päälle jos on eli "muistaa liken"
            if (task.isSuccessful) {
                val user = task.result?.toObject(UserDB::class.java)
                val favorites = user?.favorites ?: emptyList()
                if (favorites.contains(currItem.postId)) {
                    holder.favButton.isChecked = true
                }
            }
        }

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
    inner class ViewHolder(
        itemView: View,
        val favButton: CheckBox,
        val userDocRef: DocumentReference,
        private var favorites: List<String>
    ) : RecyclerView.ViewHolder(itemView) {

        val username: TextView = itemView.findViewById(R.id.tvUsername)
        val relativetime: TextView = itemView.findViewById(R.id.tvRelativetime)
        val Post: ImageView = itemView.findViewById(R.id.ivPost)
        val profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        val description: TextView = itemView.findViewById(R.id.tvDescription)

        init {
            userDocRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.toObject(UserDB::class.java)
                    favorites = user?.favorites ?: emptyList()
                    favButton.isChecked = favorites.contains(posts[adapterPosition].postId)

                    favButton.setOnCheckedChangeListener { _, isChecked ->
                        val currItem = posts[adapterPosition]
                        val post_id = currItem.postId
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userId!!)
                        userDocRef.get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = task.result?.toObject(UserDB::class.java)
                                var mutableFavorites = user?.favorites?.toMutableList() ?: mutableListOf()
                                if (isChecked) {
                                    if (!mutableFavorites.contains(post_id)) {
                                        // Add the post to the user's favorites list in Firebase
                                        mutableFavorites.add(post_id)
                                        favorites = mutableFavorites
                                        userDocRef.update("favorites", favorites)
                                    }
                                } else {
                                    if (mutableFavorites.contains(post_id)) {
                                        // Remove the post from the user's favorites list in Firebase
                                        mutableFavorites.remove(post_id)
                                        favorites = mutableFavorites
                                        userDocRef.update("favorites", favorites)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
    }