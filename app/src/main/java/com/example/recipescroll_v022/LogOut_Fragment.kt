package com.example.recipescroll_v022

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "LogOut_Fragment"

class LogOut_Fragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log_out_, container, false)
        val logOutBtn = view.findViewById<Button>(R.id.LogOutBtn)
        val cancelBtn = view.findViewById<Button>(R.id.CancelBtn)

        logOutBtn.setOnClickListener {

        }

        cancelBtn.setOnClickListener {
            Log.d(TAG, "cancel button")
            findNavController().navigate(R.id.action_logOut_Fragment_to_settingsFragment)
        }

        return view
            }


  }

