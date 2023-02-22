package com.example.tmdbchallenge.utilities

import android.widget.ImageView
import com.example.tmdbchallenge.R
import com.squareup.picasso.Picasso

object ImageHelper {

    fun loadImage(url: String, view: ImageView) {
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.baseline_movie)
            .into(view)
    }

}