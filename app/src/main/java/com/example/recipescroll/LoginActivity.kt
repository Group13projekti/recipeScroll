package com.example.recipescroll

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.recipescroll.R.layout.activity_login
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_login)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            goPostsActivity()
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnNoUser = findViewById<Button>(R.id.btnNoUser)
        val idEmail = findViewById<EditText>(R.id.idEmail)
        val idPassword = findViewById<EditText>(R.id.idPassword)

        btnNoUser.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            btnLogin.isEnabled = false
            val email = idEmail.text.toString()
            val password = idPassword.text.toString()
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase auth

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                btnLogin.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    goPostsActivity()
                } else {
                    Log.i(TAG, "signIn failed", task.exception)

                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goPostsActivity() {
        Log.i(TAG, "goPostsActivity")
        val intent = Intent(this, PostsActivity::class.java)
        startActivity(intent)
    }
}