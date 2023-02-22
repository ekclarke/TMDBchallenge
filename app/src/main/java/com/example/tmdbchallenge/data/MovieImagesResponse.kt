package com.example.tmdbchallenge.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieImagesResponse(
    val id: Int,
    val backdrops: List<Image>,
    val posters: List<Image>
)
