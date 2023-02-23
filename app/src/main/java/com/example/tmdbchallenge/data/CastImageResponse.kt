package com.example.tmdbchallenge.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CastImageResponse(
    val id: Int,
    val profiles: List<Image>
)
