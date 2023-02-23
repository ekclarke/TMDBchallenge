package com.example.tmdbchallenge.data

import android.app.Activity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class Repository {
    private val remoteData = RemoteDatasource()

    suspend fun getPopularMovies(page: Int, activity: Activity): Flow<List<MovieListing>> {
        return remoteData.getPopularMovies(page, activity).flowOn(Dispatchers.IO)
    }

    suspend fun getMovieDetails(id: Int, activity: Activity): Flow<MovieDetails> {
        return remoteData.getMovieDetails(id, activity).flowOn(Dispatchers.IO)
    }

    fun getMaxPages(): Int {
        return remoteData.maxPages
    }

    suspend fun getCredits(id: Int, activity: Activity): Flow<List<Cast>> {
        return remoteData.getCastList(id, activity).flowOn(Dispatchers.IO)
    }

    suspend fun getCastImages(id: Int, activity: Activity): Flow<List<Image>> {
        return remoteData.getCastImages(id, activity).flowOn(Dispatchers.IO)
    }

    suspend fun getMovieImages(id: Int, activity: Activity): Flow<List<Image>> {
        return remoteData.getMovieImages(id, activity).flowOn(Dispatchers.IO)
    }

    suspend fun getConfig(activity: Activity): Flow<ConfigurationResponse>{
        return remoteData.getConfig(activity)
    }
}