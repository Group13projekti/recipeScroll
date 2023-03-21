package com.example.recipescroll_v022

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

}