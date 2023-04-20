package com.example.recipescroll_v022.models

import com.google.firebase.firestore.PropertyName

data class PostDB(
    @get:PropertyName("post_id") @set:PropertyName("post_id") var postId: String = "",
    var description: String = "",
    @get:PropertyName("image_url") @set:PropertyName("image_url") var imageUrl: String = "",
    @get:PropertyName("creation_time_ms") @set:PropertyName("creation_time_ms") var creationTime: Long = 0,
    var user: UserDB? = null
)

