package com.example.recipescroll_v022


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

private const val TAG = "AccountSettingsActivity"

class AccountSettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        auth = Firebase.auth
        db = Firebase.firestore
        user = auth.currentUser!!

        val btnCloseProf = findViewById<ImageButton>(R.id.close_prfile_btn)
        val saveBtn = findViewById<ImageButton>(R.id.save_profile_btn)
        val etFullName = findViewById<EditText>(R.id.full_name_profile_frag)
        val etUserName = findViewById<EditText>(R.id.username_profile_frag)
        val etBio = findViewById<EditText>(R.id.bio_profile_frag)
        val profileImageView = findViewById<ImageView>(R.id.profile_image_view)


        // Haetaan tallennettu nimi Firestoresta ja asetetaan se editTextiin
        db.collection("users").document(user.uid)
            .get().addOnSuccessListener{ document ->
            val existingFullName = document.getString("name")
            etFullName.setText(existingFullName)
        }
        db.collection("users").document(user.uid).get().addOnSuccessListener { document ->
            val existingUsername = document.getString("uname")
            etUserName.setText(existingUsername)
        }
        db.collection("users").document(user.uid).get().addOnSuccessListener { document ->
            val existingBio = document.getString("bio")
            etBio.setText(existingBio)
        }
        db.collection("users").document(user.uid).get().addOnSuccessListener { document ->
            val profileImageUrl = document.getString("profileImageUrl")
            if (profileImageUrl != null && profileImageUrl.isNotEmpty()) {
             Picasso.get()
                 .load(profileImageUrl)
                 .into(profileImageView)
            } else {
                profileImageView.setImageResource(R.drawable.placeholder_image)
            }
        }.addOnFailureListener { expection ->
            Log.e(TAG, "Error loading profile-picture")
        }

        btnCloseProf.setOnClickListener{
            goBackActivity()
        }
        saveBtn.setOnClickListener {
            val newFullName = etFullName.text.toString()
            val newUserName = etUserName.text.toString()
            val newBio = etBio.text.toString()

            // Tarkistetaan poikkeaako uudet arvot databasessa olevien arvojen kanssa
            val updates = mutableMapOf<String, Any>() // Tehdään taulukko uusille arvoille
            db.collection("users").document(user.uid).get().addOnSuccessListener { document ->
                val existingFullName = document.getString("name") // Haetaan vanha arvo databasesta
                if (newFullName != existingFullName) { // Jos uusi arvo ja vanha arvo eivät ole samat
                    updates["name"] = newFullName     // päivitetään uusi arvo vanhan tilalle
                }

                if (newUserName.isNotBlank()) {
                    val existingUsername = document.getString("uname")
                    if (newUserName != existingUsername){
                    val query = db.collection("users").whereEqualTo("uname", newUserName)
                    query.get().addOnSuccessListener { documents ->
                        if (documents.size() > 0) {
                            Toast.makeText(this, "This username is already taken", Toast.LENGTH_SHORT).show()
                            // Käyttäjänimi on käytössä näytä virheilmoitus
                                   } else {
                            // Käyttäjänimi on käytettävissä, lisää päivitykset tietokantaan
                            updates["uname"] = newUserName
                            Toast.makeText(this, "Username changed successfull", Toast.LENGTH_SHORT).show()
                            db.collection("users").document(user.uid)
                                .update(updates)
                        }
                    }
                    }
                } else {Toast.makeText(this,
                    "Username cannot be empty", Toast.LENGTH_SHORT).show()}


                val existingBio = document.getString("bio")
                if (newBio != existingBio) {
                    updates["bio"] = newBio
                }

                // Päivitetään databaseen uudet arvot
                if (updates.isNotEmpty()) {
                    db.collection("users").document(user.uid)
                        .update(updates)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    private fun goBackActivity() {
        val intent = Intent(this, profile::class.java)
        startActivity(intent)
    }
}