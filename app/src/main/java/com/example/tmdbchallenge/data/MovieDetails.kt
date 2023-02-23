package com.example.tmdbchallenge.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetails(
    val backdrop_path: String?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String?,
    val original_language: String,
    val original_title: String,
    val overview: String?,
    val poster_path: String?,
    val release_date: String,
    val runtime: Int?,
    val spoken_languages: List<SpokenLanguage>,
    val status: Status,
    val tagline: String?,
    val title: String
)

@JsonClass(generateAdapter = true)
data class Genre(
    val id: Int,
    val name: String
)

@JsonClass(generateAdapter = true)
data class SpokenLanguage(
    val iso_639_1: String,
    val name: String
)

enum class Status(val status: String) {
    @Json(name = "Rumored")
    RUMORED("Rumored"),

    @Json(name = "Planned")
    PLANNED("Planned"),

    @Json(name = "In Production")
    IN_PROD("In Production"),

    @Json(name = "Post Production")
    POST_PROD("Post Production"),

    @Json(name = "Released")
    RELEASED("Released"),

    @Json(name = "Canceled")
    CANCELED("Canceled")
}

