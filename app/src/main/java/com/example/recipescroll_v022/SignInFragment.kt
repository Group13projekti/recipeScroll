package com.example.recipescroll_v022

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        val button = view.findViewById<Button>(R.id.backbtn)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
        }

        return view
    }

}