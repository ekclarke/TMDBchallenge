package com.example.tmdbchallenge.data

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ConfigurationResponse(
    val images: Configuration,
    val change_keys: List<String>
)

@JsonClass(generateAdapter = true)
data class Configuration(
    val base_url: String,
    val secure_base_url: String,
    val backdrop_sizes: List<String>,
    val logo_sizes: List<String>,
    val poster_sizes: List<String>,
    val profile_sizes: List<String>,
    val still_sizes: List<String>
)
