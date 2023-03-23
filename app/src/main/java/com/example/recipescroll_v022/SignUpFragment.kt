package com.example.recipescroll_v022

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SingUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sing_up, container, false)
        val button = view.findViewById<Button>(R.id.backbtn)

        auth = Firebase.auth

        val txtEmail = view.findViewById<EditText>(R.id.email)
        val txtPassword = view.findViewById<EditText>(R.id.password)
        val btnSubmit =  view.findViewById<Button>(R.id.submitsignupbtn)
        val txtName = view.findViewById<EditText>(R.id.name)
        val txtUname = view.findViewById<EditText>(R.id.username)

        btnSubmit.setOnClickListener {
            btnSubmit.isEnabled = false
            val sEmail = txtEmail.text.toString()
            val sPassword = txtPassword.text.toString()
            val sName = txtName.text.toString()
            val sUname = txtUname.text.toString()

            if (sEmail.isBlank() || sPassword.isBlank()) {
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(requireActivity()) { task ->

                    if (task.isSuccessful) {
                        //If sign in successful, log success to console and write user to database

                        database = FirebaseDatabase.getInstance().getReference("Users")
                        val user = userDB(sName, sUname, sEmail)
                        database.child(sUname).setValue(user).addOnSuccessListener {
                            Log.d(ContentValues.TAG,"DatabaseInsert:success" )
                        }.addOnFailureListener {
                            Log.w(ContentValues.TAG, "DatabaseInsert:Failure", task.exception)
                        }

                        Log.d(ContentValues.TAG, "createUserWithPassword:success")
                        Toast.makeText(requireActivity(), "Sign up successful", Toast.LENGTH_SHORT).show()
                        //TODO("link to feedpage")
                    } else {
                        //if sign in not successful, log failure to console

                        Log.w(ContentValues.TAG, sEmail, task.exception)
                        Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_SHORT).show()

                    }
                }
        }

        button.setOnClickListener {
            findNavController().navigate(R.id.action_singUpFragment_to_homeFragment)
        }

        return view
    }

}

