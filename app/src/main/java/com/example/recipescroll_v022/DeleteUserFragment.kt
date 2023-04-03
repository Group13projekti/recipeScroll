package com.example.recipescroll_v022

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "DeleteUserFragment"

class DeleteUserFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_delete_user, container, false)
        val DeleteBtn = view.findViewById<Button>(R.id.DeleteBtn)
        val Cancel1Btn = view.findViewById<Button>(R.id.Cancel1Btn)
        val checkBox = view.findViewById<CheckBox>(R.id.checkBox1)

        DeleteBtn.setOnClickListener {
            val user = Firebase.auth.currentUser!!
            val credential = EmailAuthProvider
                .getCredential("user@example.com", "password1234")
            user.reauthenticate(credential)
                .addOnCompleteListener { Log.d(TAG, "User re-authenticated.") }

            checkBox
                .setOnCheckedChangeListener { buttonView, isChecked ->
                    Log.d("CHECKBOXES", "Agreement is checked: $isChecked")


            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Account deleted.")
                    }
                }
        }}

        Cancel1Btn.setOnClickListener {
            Log.d(TAG, "cancel button")
            findNavController().navigate(R.id.action_logOut_Fragment_to_settingsFragment)
        }

        return view
    }


}