package com.example.recipescroll_v022

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class profile : Fragment() {
    private lateinit var usernameTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var bioTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        usernameTextView = view.findViewById(R.id.usernameTextView)
        nameTextView = view.findViewById(R.id.nameTextView)
        bioTextView = view.findViewById(R.id.bioTextView)
        return view
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val db = Firebase.firestore
            db.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val username = document.getString("uname")
                        usernameTextView.text = username
                        val name = document.getString("name")
                        nameTextView.text = name
                        val bio = document.getString("bio")
                        bioTextView.text = bio
                    }
                }
                .addOnFailureListener { exception ->
                    // handle exception
                }
        }
    }
}
