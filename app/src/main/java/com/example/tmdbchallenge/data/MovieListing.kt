package com.example.tmdbchallenge.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResponse(
    val page: Int,
    val results: List<MovieListing>,
    val total_results: Int,
    val total_pages: Int
)

@JsonClass(generateAdapter = true)
data class MovieListing(
    val poster_path: String?,
    val adult: Boolean,
    val overview: String,
    val release_date: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_title: String,
    val original_language: String,
    val title: String,
    val backdrop_path: String?,
    val popularity: Float,
    val vote_count: Int,
    val video: Boolean,
    val vote_average: Float)



