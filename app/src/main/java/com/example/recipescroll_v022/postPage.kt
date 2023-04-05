package com.example.recipescroll_v022

import android.content.Intent
import android.graphics.BitmapFactory
import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.GridLayout
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import java.io.IOException


private const val TAG = "PostPage"
private const val PICK_PHOTO_CODE = 6334

class PostPage : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var storage: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_page, container, false)
        val displayMetrics = resources.displayMetrics

        val imageView = view.findViewById<ImageView>(R.id.uploadedImage)
        val imagePickButton = view.findViewById<Button>(R.id.imageUpload)
        val dryButton = view.findViewById<Button>(R.id.dryText)
        val wetButton = view.findViewById<Button>(R.id.wetText)
        val spiceButton = view.findViewById<Button>(R.id.spiceText)
        val dairyButton = view.findViewById<Button>(R.id.dairyText)
        val proButton = view.findViewById<Button>(R.id.proText)
        val aroButton = view.findViewById<Button>(R.id.aroText)

        val dryIngredientsList = view.findViewById<GridLayout>(R.id.dryIngredientList)
        val wetIngredientsList = view.findViewById<GridLayout>(R.id.wetIngredientList)
        val spiceList = view.findViewById<GridLayout>(R.id.spiceList)
        val dairyList = view.findViewById<GridLayout>(R.id.dairyList)
        val proList = view.findViewById<GridLayout>(R.id.proList)
        val aroList = view.findViewById<GridLayout>(R.id.aroList)

        val db = Firebase.firestore
        val dryRef = db.collection("ingredients").document("dry_ingredients")
        val wetRef = db.collection("ingredients").document("wet_ingredients")
        val spiceRef = db.collection("ingredients").document("spices")
        val dairyRef = db.collection("ingredients").document("dairy")
        val proRef = db.collection("ingredients").document("produce")
        val aroRef = db.collection("ingredients").document("aromatics")

        var dryV = false
        var wetV = false
        var spiceV = false
        var dairyV = false
        var proV = false
        var aroV = false

        dryIngredientsList.visibility = View.GONE
        wetIngredientsList.visibility = View.GONE
        spiceList.visibility = View.GONE
        dairyList.visibility = View.GONE
        proList.visibility = View.GONE
        aroList.visibility = View.GONE

        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val checkboxSize = minOf(screenWidth, screenHeight) / 4

        imagePickButton.setOnClickListener {
            // Get image from external storage
            val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
            try {
                val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inputStream?.close()
            }
        }

        dryButton.setOnClickListener {
            //toggle visibility of ingredients
            dryV = !dryV
            dryIngredientsList.visibility = if (dryV) View.VISIBLE else View.GONE
        }

        wetButton.setOnClickListener {
            wetV = !wetV
            wetIngredientsList.visibility = if (wetV) View.VISIBLE else View.GONE
        }

        spiceButton.setOnClickListener {
            spiceV = !spiceV
            spiceList.visibility = if (spiceV) View.VISIBLE else View.GONE
        }

        dairyButton.setOnClickListener {
            dairyV = !dairyV
            dairyList.visibility = if (dairyV) View.VISIBLE else View.GONE
        }

        proButton.setOnClickListener {
            proV = !proV
            proList.visibility = if (proV) View.VISIBLE else View.GONE
        }

        aroButton.setOnClickListener {
            aroV = !aroV
            aroList.visibility = if (aroV) View.VISIBLE else View.GONE
        }

        dryRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    Log.d(TAG, "Found documentSnapshot!")
                    val data = documentSnapshot.data
                    if (data != null) {
                        Log.d(TAG, "Found data!")
                        for ((key, value) in data) {
                            if (value is String) {
                                val checkBox = CheckBox(requireContext())
                                checkBox.text = data[key].toString()
                                checkBox.isChecked = false
                                checkBox.layoutParams = centerParams(checkboxSize, checkboxSize)
                                dryIngredientsList.addView(checkBox)
                            }
                        }

                    }
                }
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Error getting document: ", exception)
                return@addOnFailureListener
            }

        wetRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    Log.d(TAG, "Found documentSnapshot!")
                    val data = documentSnapshot.data
                    if (data != null) {
                        Log.d(TAG, "Found data!")
                        for ((key, value) in data) {
                            if (value is String) {
                                val checkBox = CheckBox(requireContext())
                                checkBox.text = data[key].toString()
                                checkBox.isChecked = false
                                checkBox.layoutParams = centerParams(checkboxSize, checkboxSize)
                                wetIngredientsList.addView(checkBox)
                            }
                        }

                    }
                }
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Error getting document: ", exception)
                return@addOnFailureListener
            }

        spiceRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    Log.d(TAG, "Found documentSnapshot!")
                    val data = documentSnapshot.data
                    if (data != null) {
                        Log.d(TAG, "Found data!")
                        for ((key, value) in data) {
                            if (value is String) {
                                val checkBox = CheckBox(requireContext())
                                checkBox.text = data[key].toString()
                                checkBox.isChecked = false
                                checkBox.layoutParams = centerParams(checkboxSize, checkboxSize)
                                spiceList.addView(checkBox)
                            }
                        }

                    }
                }
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Error getting document: ", exception)
                return@addOnFailureListener
            }

        dairyRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    Log.d(TAG, "Found documentSnapshot!")
                    val data = documentSnapshot.data
                    if (data != null) {
                        Log.d(TAG, "Found data!")
                        for ((key, value) in data) {
                            if (value is String) {
                                val checkBox = CheckBox(requireContext())
                                checkBox.text = data[key].toString()
                                checkBox.isChecked = false
                                checkBox.layoutParams = centerParams(checkboxSize, checkboxSize)
                                dairyList.addView(checkBox)
                            }
                        }

                    }
                }
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Error getting document: ", exception)
                return@addOnFailureListener
            }

        proRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    Log.d(TAG, "Found documentSnapshot!")
                    val data = documentSnapshot.data
                    if (data != null) {
                        Log.d(TAG, "Found data!")
                        for ((key, value) in data) {
                            if (value is String) {
                                val checkBox = CheckBox(requireContext())
                                checkBox.text = data[key].toString()
                                checkBox.isChecked = false
                                checkBox.layoutParams = centerParams(checkboxSize, checkboxSize)
                                proList.addView(checkBox)
                            }
                        }

                    }
                }
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Error getting document: ", exception)
                return@addOnFailureListener
            }

        aroRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    Log.d(TAG, "Found documentSnapshot!")
                    val data = documentSnapshot.data
                    if (data != null) {
                        Log.d(TAG, "Found data!")
                        for ((key, value) in data) {
                            if (value is String) {
                                val checkBox = CheckBox(requireContext())
                                checkBox.text = data[key].toString()
                                checkBox.isChecked = false
                                checkBox.layoutParams = centerParams(checkboxSize, checkboxSize)
                                aroList.addView(checkBox)
                            }
                        }

                    }
                }
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Error getting document: ", exception)
                return@addOnFailureListener
            }


        return view
    }
}

fun centerParams(width: Int, height: Int): GridLayout.LayoutParams {
    val params = GridLayout.LayoutParams()
    params.width = width
    params.height = height
    params.setGravity(Gravity.CENTER)
    return params
}
