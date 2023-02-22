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
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String
    ): Response<MovieDetails>

    //TODO: get cast
    //TODO: get cast images

}


