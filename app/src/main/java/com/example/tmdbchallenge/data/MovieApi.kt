package com.example.tmdbchallenge.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("api_key") api_key: String
    ): Response<MovieResponse>

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): Response<MovieDetails>

    @GET("3/movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): Response<Credits>

    @GET("3/person/{person_id}/images")
    suspend fun getCastImages(
        @Path("person_id") person_id: Int,
        @Query("api_key") api_key: String
    ): Response<CastImageResponse>

    @GET("3/movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): Response<MovieImagesResponse>

    @GET("3/configuration")
    suspend fun getConfig(
        @Query("api_key") api_key: String
    ): Response<ConfigurationResponse>

}


