package com.example.tmdbchallenge.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class Repository {
    private val remoteData = RemoteDatasource()

    suspend fun getPopularMovies(page: Int): Flow<List<MovieListing>> {
        return remoteData.getPopularMovies(page).flowOn(Dispatchers.IO)
    }

    suspend fun getMovieDetails(id: Int): Flow<MovieDetails> {
        return remoteData.getMovieDetails(id).flowOn(Dispatchers.IO)
    }

    fun getMaxPages(): Int {
        return remoteData.maxPages
    }

    suspend fun getCredits(id: Int): Flow<List<Cast>> {
        return remoteData.getCastList(id).flowOn(Dispatchers.IO)
    }

    suspend fun getCastImages(id: Int): Flow<List<Image>> {
        return remoteData.getCastImages(id).flowOn(Dispatchers.IO)
    }

    suspend fun getMovieImages(id: Int): Flow<List<Image>> {
        return remoteData.getMovieImages(id).flowOn(Dispatchers.IO)
    }
}