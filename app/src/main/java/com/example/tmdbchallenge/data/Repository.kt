package com.example.tmdbchallenge.data

import kotlinx.coroutines.flow.Flow

class Repository {
    private val remoteData = RemoteDatasource()

    suspend fun getPopularMovies(page: Int): Flow<List<MovieListing>> {
        return remoteData.getPopularMovies(page)
    }

    suspend fun getMovieDetails(id: Int): Flow<MovieDetails> {
        return remoteData.getMovieDetails(id)
    }

    fun getMaxPages(): Int {
        return remoteData.maxPages
    }

    suspend fun getConfig(): Flow<ConfigurationResponse> {
        return remoteData.getConfig()
    }

    suspend fun getCredits(id: Int): Flow<List<Cast>> {
        return remoteData.getCastList(id)
    }

    suspend fun getCastImages(id: Int): Flow<List<Image>> {
        return remoteData.getCastImages(id)
    }

    suspend fun getMovieImages(id: Int): Flow<List<Image>>{
        return remoteData.getMovieImages(id)
    }


}