package com.example.recipescroll_v022.models

import com.google.firebase.firestore.PropertyName

data class UserDB(
    var name : String? = null,
    val uname : String? = null,
    val email: String? = null,
    @get:PropertyName("profileImageUrl") @set:PropertyName("profileImageUrl")
    var profileImageUrl: String = "",
)
