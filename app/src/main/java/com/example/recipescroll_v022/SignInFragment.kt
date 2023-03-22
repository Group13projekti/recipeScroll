package com.example.recipescroll_v022

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "SignInFragment"

class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        val btnBack = view.findViewById<Button>(R.id.backbtn)

        val btnLogin = view.findViewById<Button>(R.id.submitloginbtn)
        val txtEmail = view.findViewById<EditText>(R.id.email)
        val txtPassword = view.findViewById<EditText>(R.id.password)

        btnLogin.setOnClickListener {
            val uname = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            Log.d(TAG, uname)
            Log.d(TAG, password)

            if (uname.isBlank() || password.isBlank()) {
                Toast.makeText(requireActivity(), "Email or password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Firebase auth
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(uname,password).addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Sign in successful")
                    Toast.makeText(requireActivity(), "Logged in", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signInFragment_to_postFragment)
                } else {
                    Log.i(TAG, "Sign in failed", task.exception)
                    Toast.makeText(requireActivity(), "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnBack.setOnClickListener {
            Log.d(TAG, "back button")
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
        }

        return view
    }

}