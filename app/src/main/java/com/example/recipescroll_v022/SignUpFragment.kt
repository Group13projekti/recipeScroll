package com.example.recipescroll_v022

import android.content.Intent
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
import com.example.recipescroll_v022.models.UserDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

private const val TAG = "SignUpFragment"

class SingUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

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

            if (sEmail.isBlank() || sPassword.isBlank() || sName.isBlank() || sUname.isBlank()) {
                return@setOnClickListener
            }

            database = FirebaseFirestore.getInstance()
            database.collection("users")
                .whereEqualTo("uname", sUname)
                .get()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        for (document in task.result) {
                            if (document.exists()) {
                                Log.d(TAG, "UsernameCheck: Already exists!")
                                Toast.makeText(requireActivity(), "Username already exists!", Toast.LENGTH_SHORT).show()
                                btnSubmit.setOnClickListener(null)
                            } else {
                                Log.d(TAG, "UsernameCheck: Username available")
                            }
                        }
                    } else {
                        Log.d(TAG, "ErrorGettingDocuments: ", task.exception)
                    }
                }

            auth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(requireActivity()) { task ->

                    if (task.isSuccessful) {
                        //If sign in successful, log success to console and write user to database
                        val uidUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        database = FirebaseFirestore.getInstance()
                        val user = UserDB(sName, sUname, sEmail)
                        database.collection("users").document(uidUser)
                            .set(user)
                            .addOnSuccessListener {
                            Log.d(TAG,"DatabaseInsert:success" )
                        }.addOnFailureListener {
                            Log.w(TAG, "DatabaseInsert:Failure", task.exception)
                                return@addOnFailureListener
                        }

                        Log.d(TAG, "createUserWithPassword:success")
                        Toast.makeText(requireActivity(), "Sign up successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this.context, FeedActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        //if sign in not successful, log failure to console
                        btnSubmit.isEnabled = true
                        Log.w(TAG, sEmail, task.exception)
                        Toast.makeText(requireActivity(), "Something went wrong: " + task.exception, Toast.LENGTH_SHORT).show()

                    }
                }
        }

        button.setOnClickListener {
            findNavController().navigate(R.id.action_singUpFragment_to_homeFragment)
        }

        return view
    }

}

