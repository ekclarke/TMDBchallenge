package com.example.tmdbchallenge.data

import com.example.tmdbchallenge.data.MovieDetails
import com.example.tmdbchallenge.data.MovieListing
import com.example.tmdbchallenge.data.RemoteDatasource
import kotlinx.coroutines.flow.Flow

class Repository {
    private val remoteData = RemoteDatasource()

    suspend fun getPopularMovies(page: Int): Flow<List<MovieListing>> {
        return remoteData.getPopularMovies(page)
    }

    suspend fun getMovieDetails(id: Int): Flow<MovieDetails> {
        return remoteData.getMovieDetails(id)
    }

    fun getMaxPages(): Int{
        return remoteData.maxPages
    }
}