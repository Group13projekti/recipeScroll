package com.example.recipescroll_v022

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipescroll_v022.models.PostDB
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

private const val TAG = "FontPage"
class FrontPage : Fragment()  {
    private lateinit var firestoreDb : FirebaseFirestore
    private lateinit var posts : MutableList<PostDB>
    private lateinit var adapter: PostsAdapter
    private lateinit var rvPosts: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        posts = mutableListOf()
        adapter = PostsAdapter(requireContext(), posts)
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(requireContext())


        firestoreDb = FirebaseFirestore.getInstance()
        val postsReference = firestoreDb
            .collection("posts")
            .limit(30) //Postauksien määrää rajoitetaan frontpagella
            .orderBy("creation_time_ms", Query.Direction.DESCENDING) //Postaukset näytetään uusimmasta lähtien
        postsReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(PostDB::class.java)
            for (post in postList) {
                Log.i(TAG, "Post ${post}")
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_front_page, container, false)
    }

}
