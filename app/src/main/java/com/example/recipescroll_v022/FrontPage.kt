import com.example.recipescroll_v022.PostAdapter
import com.example.recipescroll_v022.R

</androidx.cardview.widget.CardView>
package com.example.recipescroll_v022

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

class FrontPage : Fragment() {

    private lateinit var posts: MutableList<Post>
    private lateinit var adapter: PostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        posts = mutableListOf()
        adapter = PostAdapter(this, posts)
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(this)

    }
    //firebase connectioon posts.addAll(postList) ja adapter.notifyDataSetChanged() ja posts.clear()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_front_page, container, false)
    }
}
