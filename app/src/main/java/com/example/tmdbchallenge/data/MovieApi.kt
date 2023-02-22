package com.example.tmdbchallenge.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("/movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("api_key") api_key: String
    ): Response<MovieResponse>

    @GET("/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): Response<MovieDetails>

    @GET("/movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): Response<Credits>

    @GET("/person/{person_id}/images")
    suspend fun getCastImages(
        @Path("person_id") person_id: Int,
        @Query("api_key") api_key: String
    ): Response<CastImageResponse>

    @GET("/configuration")
    suspend fun getConfig(
        @Query("api_key") api_key: String
    ): Response<ConfigurationResponse>

    @GET("/movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): Response<MovieImagesResponse>

}


