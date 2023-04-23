package com.example.recipescroll_v022

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "LogOutFragment"

class LogOutFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log_out, container, false)
        val logOutBtn = view.findViewById<Button>(R.id.LogOutBtn)
        val cancelBtn = view.findViewById<Button>(R.id.CancelBtn)



        logOutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this.context, FeedActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            findNavController().navigate(R.id.action_LogOutFragment_to_HomeFragment)
        }

        cancelBtn.setOnClickListener {
            Log.d(TAG, "cancel button")
            val navController = findNavController()
            findNavController().navigate(R.id.action_LogOutFragment_to_SettingsFragment)
        }

        return view
            }


  }

