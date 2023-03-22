package com.example.recipescroll

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.*
import com.example.recipescroll.R.layout.activity_signup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val TAG = "SignupActivity"

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_signup)

        auth = FirebaseAuth.getInstance()

        val btnLogin = findViewById<Button>(R.id.btnSignup)
        val idEmail = findViewById<EditText>(R.id.idEmail)
        val idPassword = findViewById<EditText>(R.id.idPassword)

        btnLogin.setOnClickListener {
            btnLogin.isEnabled = false
            val email = idEmail.text.toString()
            val password = idPassword.text.toString()
            if (email.isBlank() || password.isBlank()) {
                return@setOnClickListener
            }

            // Firebase auth

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        goPostsActivity(user)
                    } else {
                        Log.i(TAG, "createUserWithEmailAndPassword failed", task.exception)
                        btnLogin.isEnabled = true
                    }
                }
        }
    }

    private fun goPostsActivity(user: FirebaseUser?) {
        Log.i(TAG, "goPostsActivity")
        val intent = Intent(this, PostsActivity::class.java)
        startActivity(intent)
        finish()
    }
}
