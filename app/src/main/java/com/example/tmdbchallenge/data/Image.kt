package com.example.tmdbchallenge.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image(    val aspect_ratio: Float,
                     val file_path: String,
                     val height: Int,
                     val width: Int)
