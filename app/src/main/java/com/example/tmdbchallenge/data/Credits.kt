package com.example.tmdbchallenge.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Credits(
    val id: Int,
    val cast: List<Cast>,
    val crew: List<Crew>
)

@JsonClass(generateAdapter = true)
data class Cast(
    val adult: Boolean,
    val gender: Int?,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Float,
    val profile_path: String?,
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val order: Int

)

@JsonClass(generateAdapter = true)
data class Crew(
    val adult: Boolean,
    val gender: Int?,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Float,
    val profile_path: String?,
    val credit_id: String,
    val department: String,
    val job: String
)
