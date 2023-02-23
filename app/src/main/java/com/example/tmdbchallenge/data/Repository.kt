package com.example.tmdbchallenge.data

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class Repository {
    private val remoteData = RemoteDatasource()

    suspend fun getPopularMovies(page: Int, context: Context): Flow<List<MovieListing>> {
        return remoteData.getPopularMovies(page, context).flowOn(Dispatchers.IO)
    }

    suspend fun getMovieDetails(id: Int, context: Context): Flow<MovieDetails> {
        return remoteData.getMovieDetails(id, context).flowOn(Dispatchers.IO)
    }

    fun getMaxPages(): Int {
        return remoteData.maxPages
    }

    suspend fun getCredits(id: Int, context: Context): Flow<List<Cast>> {
        return remoteData.getCastList(id, context).flowOn(Dispatchers.IO)
    }

    suspend fun getCastImages(id: Int, context: Context): Flow<List<Image>> {
        return remoteData.getCastImages(id, context).flowOn(Dispatchers.IO)
    }

    suspend fun getMovieImages(id: Int, context: Context): Flow<List<Image>> {
        return remoteData.getMovieImages(id, context).flowOn(Dispatchers.IO)
    }

    suspend fun getConfig(activity: Activity): Flow<ConfigurationResponse>{
        return remoteData.getConfig(activity)
    }
}