package com.example.recipescroll_v022

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipescroll_v022.models.PostDB
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query



class favorites : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
       // val postsView = view.findViewById<RecyclerView>(R.id.rvPosts)

     /*   posts = mutableListOf()
        adapter = PostAdapter(this, posts)
        postsView.adapter = adapter
        postsView.layoutManager = LinearLayoutManager(context)

        firestoreDb = FirebaseFirestore.getInstance()
        val postReference = firestoreDb
            .collection("posts")
            .limit(20)
            .orderBy("creation_time_ms" , Query.Direction.DESCENDING)
        postReference.addSnapshotListener { snapshot, exeption ->
            if (exeption != null  || snapshot == null){
                Log.e(TAG, "Exeption when querying post" , exeption)
                return@addSnapshotListener
            }
            posts.clear()
            posts.addAll(snapshot.toObjects(PostDB::class.java))
            adapter.notifyDataSetChanged()
        }*/

        return view
    }
}