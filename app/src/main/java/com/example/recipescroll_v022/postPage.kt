package com.example.recipescroll_v022

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.recipescroll_v022.loaders.ImageLoader
import com.example.recipescroll_v022.models.PostDB
import com.example.recipescroll_v022.models.UserDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


private const val TAG = "PostPage"

class PostPage : Fragment() {

    private lateinit var imageLoader: ImageLoader
    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_page, container, false)
        val displayMetrics = resources.displayMetrics
        val curUserUid = FirebaseAuth.getInstance().currentUser?.uid

        val imageView = view.findViewById<ImageView>(R.id.uploadedImage)
        val imagePickButton = view.findViewById<Button>(R.id.imageUpload)
        val foodDesc = view.findViewById<EditText>(R.id.postDescription)
        val stepButton = view.findViewById<Button>(R.id.stepBtn)
        val dryButton = view.findViewById<Button>(R.id.dryText)
        val wetButton = view.findViewById<Button>(R.id.wetText)
        val spiceButton = view.findViewById<Button>(R.id.spiceText)
        val dairyButton = view.findViewById<Button>(R.id.dairyText)
        val proButton = view.findViewById<Button>(R.id.proText)
        val aroButton = view.findViewById<Button>(R.id.aroText)
        val subButton = view.findViewById<Button>(R.id.btnSubmit)

        val stepsList = view.findViewById<EditText>(R.id.stepList)
        val dryIngredientsList = view.findViewById<GridLayout>(R.id.dryIngredientList)
        val wetIngredientsList = view.findViewById<GridLayout>(R.id.wetIngredientList)
        val spiceList = view.findViewById<GridLayout>(R.id.spiceList)
        val dairyList = view.findViewById<GridLayout>(R.id.dairyList)
        val proList = view.findViewById<GridLayout>(R.id.proList)
        val aroList = view.findViewById<GridLayout>(R.id.aroList)

        val db = Firebase.firestore
        val dbPosts = db.collection("posts")
        val dbUser = db.collection("users").document(curUserUid!!)
        val dryRef = db.collection("ingredients").document("dry_ingredients")
        val wetRef = db.collection("ingredients").document("wet_ingredients")
        val spiceRef = db.collection("ingredients").document("spices")
        val dairyRef = db.collection("ingredients").document("dairy")
        val proRef = db.collection("ingredients").document("produce")
        val aroRef = db.collection("ingredients").document("aromatics")

        var stepV = false
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

        var imageUri = ""
        val ingLists = mutableListOf(dryIngredientsList, wetIngredientsList, spiceList, dairyList, proList, aroList)
        val checkedItems = mutableListOf<String>()
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val checkboxSize = minOf(screenWidth, screenHeight) / 4

        imageLoader = ImageLoader(requireContext())

        selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    imageLoader.loadImage(selectedImageUri.toString(), imageView)
                    imageUri = selectedImageUri.toString()
                }
            }
        }

        var currUname: String? = null
        var profilePic = ""

        dbUser.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    currUname = document.getString("uname")
                    profilePic = document.getString("profileImageUrl").toString()
                }
            }

        imagePickButton.setOnClickListener {
            // Get image from external storage
            openGalleryForImage()
        }

        stepButton.setOnClickListener {
            stepV = !stepV
            stepsList.visibility = if (stepV) View.VISIBLE else View.GONE
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

        subButton.setOnClickListener {
            val sDesc = foodDesc.text.toString()
            val sImage = imageUri
            val sInstructions = stepsList.text.toString()
            val sTime: Long = System.currentTimeMillis()
            for (i in 0 until ingLists.count()) {
                val ingCount = ingLists.elementAt(i)
                for (x in 0 until ingCount.childCount) {
                    val views = ingCount.getChildAt(x)
                    if (views is CheckBox && views.isChecked) {
                        checkedItems.add(views.text.toString())
                    }
                }
            }
            val dataPost = PostDB(sDesc, sImage, sTime, sInstructions, checkedItems, UserDB(uname = currUname, profileImageUrl = profilePic ))

            dbPosts.add(dataPost)
                .addOnSuccessListener { documentRef ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentRef.id}")
                    Toast.makeText(requireActivity(), "Post submitted!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(requireActivity(), FeedActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding document", e)
                }
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
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectImageLauncher.launch(intent)
    }
}

fun centerParams(width: Int, height: Int): GridLayout.LayoutParams {
    val params = GridLayout.LayoutParams()
    params.width = width
    params.height = height
    params.setGravity(Gravity.CENTER)
    return params
}
