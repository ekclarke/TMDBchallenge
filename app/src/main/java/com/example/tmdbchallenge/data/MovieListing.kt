package com.example.tmdbchallenge.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResponse(
    val results: List<MovieListing>,
    val total_pages: Int
)

@JsonClass(generateAdapter = true)
data class MovieListing(
    val poster_path: String?,
    val overview: String,
    val release_date: String,
    val id: Int,
    val title: String,
    val backdrop_path: String?,
    val vote_average: Float)



