package com.example.recipescroll_v022


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.widget.*

import com.google.firebase.storage.ktx.storage
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.recipescroll_v022.loaders.ImageLoader
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.*


private const val TAG = "AccountSettingsActivity"

class AccountSettingsActivity : AppCompatActivity() {

    private lateinit var imageLoader: ImageLoader
    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent>
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
        //val logOutBtn = findViewById<Button>(R.id.logout_btn)
        //val delAccBtn = findViewById<Button>(R.id.delete_account_btn)
        val changeImageBtn = findViewById<Button>(R.id.change_image_text_btn)
        val storage = Firebase.storage
        val randomString = generateRandomString(10)
        val storageRef = storage.reference.child("profilepictures").child(randomString)


        // Haetaan tallennettu nimi Firestoresta ja asetetaan se editTextiin
        db.collection("users").document(user.uid).get().addOnSuccessListener{ document ->
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

        //nykyinen profiilikuva
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

        var imageUri = ""
        var nweUrl = ""
        imageLoader = ImageLoader(this)
        selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    imageLoader.loadImage(selectedImageUri.toString(), profileImageView)
                    storageRef.putFile(selectedImageUri!!)
                        .addOnSuccessListener { taskSnapshot ->
                            Log.d(TAG, "Kuva tallennettu onnistuneesti!")
                            storageRef.downloadUrl
                                .addOnSuccessListener { uri ->
                                    nweUrl = uri.toString()

                                }
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Virhe tallennettaessa kuvaa: $exception")
                        }

                }
            }
        }


        changeImageBtn.setOnClickListener{
            openGalleryForImage()
        }



        saveBtn.setOnClickListener {
            val newFullName = etFullName.text.toString()
            val newUserName = etUserName.text.toString()
            val newBio = etBio.text.toString()
            val newUrl = nweUrl



            // Tarkistetaan poikkeaako uudet arvot databasessa olevien arvojen kanssa
            val updates = mutableMapOf<String, Any>() // Tehdään taulukko uusille arvoille
            db.collection("users").document(user.uid).get().addOnSuccessListener { document ->
                val existingFullName = document.getString("name") // Haetaan vanha arvo databasesta
                if (newFullName != existingFullName) { // Jos uusi arvo ja vanha arvo eivät ole samat
                    updates["name"] = newFullName     // päivitetään uusi arvo vanhan tilalle
                }
                val existingBio = document.getString("bio")
                if (newBio != existingBio) {
                    updates["bio"] = newBio
                }

                val existingUrl = document.getString("profileImageUrl")
                if (newUrl != existingUrl) {
                    updates["profileImageUrl"] = nweUrl
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
                                db.collection("users").document(user.uid)
                                    .update(updates)
                            }
                        }
                    }
                } else {Toast.makeText(this,
                    "Username cannot be empty", Toast.LENGTH_SHORT).show()}

                // haetaan randomstringillä kuva databasesta ja otetaan sen url ja laitetaan se url ["profileImageUrl"]

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
        /*
        logOutBtn.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Log Out")
            builder.setMessage("Do you really want to log out?")
            builder.setPositiveButton("Yes") { _, _ ->
                FirebaseAuth.getInstance().signOut() // Kirjaa käyttäjän ulos Firebase Auth -palvelusta
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // Tyhjentää pinoon liittyvät aktiviteetit ja käynnistää uudelleen
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Sulje AlertDialog
            }
            val dialog = builder.create()
            dialog.show()
        }
        delAccBtn.setOnClickListener{
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Account")
                builder.setMessage("Do you really want to delete your account? " +
                        "Confirm by setting your password.")
                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                builder.setView(input)
                builder.setPositiveButton("Yes") { _, _ ->
                    val credential = EmailAuthProvider.getCredential(user.email!!, input.text.toString())
                    user.reauthenticate(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            user?.delete()?.addOnCompleteListener { task ->
                                //user.delete().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(applicationContext, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Could not delete", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            } else {
                Toast.makeText(this, "Käyttäjätiliä ei löytynyt", Toast.LENGTH_SHORT).show()
            }
        }
        */
    }
    fun generateRandomString(length: Int): String {
        val allowedChars = "abcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random()
        return (1..length)
            .map { allowedChars[random.nextInt(allowedChars.length)] }
            .joinToString("")
    }
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectImageLauncher.launch(intent)
    }
    private fun goBackActivity() {
        val intent = Intent(this, profile::class.java)
        startActivity(intent)
    }
}