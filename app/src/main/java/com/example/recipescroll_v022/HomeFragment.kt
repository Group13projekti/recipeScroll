package com.example.recipescroll_v022


import android.content.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val auth = FirebaseAuth.getInstance()

        // Remember user and skip login/signup phase
        if (auth.currentUser != null) {
            goFeedActivity()
        }
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val signinbutton = view.findViewById<Button>(R.id.signinbtn)
        val signupbutton = view.findViewById<Button>(R.id.signupbtn)

        signinbutton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
        }
        signupbutton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sing_up)
        }

        return view
    }


    fun goFeedActivity() {
        val intent = Intent(activity, FeedActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}