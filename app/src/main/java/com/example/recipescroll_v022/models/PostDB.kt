package com.example.recipescroll_v022.models

import com.google.firebase.firestore.PropertyName

data class PostDB(
    var description: String = "",
    @get:PropertyName("image_url") @set:PropertyName("image_url") var imageUrl: String = "",
    @get:PropertyName("creation_time_ms") @set:PropertyName("creation_time_ms") var creationTime: Long = 0,
    var ingredients: List<String>? = null,
    var user: UserDB? = null
)

