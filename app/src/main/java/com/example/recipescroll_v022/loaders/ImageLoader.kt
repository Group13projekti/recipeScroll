package com.example.recipescroll_v022.loaders

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ImageLoader(private val context: Context) {

    fun loadImage(imageUri: String?, imageView:ImageView) {
        Glide.with(context)
            .load(imageUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }
}