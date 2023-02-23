package com.example.tmdbchallenge.data

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//TODO: handle network connectivity and other errors on the UI
class RemoteDatasource {
    companion object {
        const val API_KEY = "5de8bf9fa1e91b52da0573d1e5263eb2"
    }

    var maxPages = -1

    private val client: OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(getLoggingInterceptor())
            .build()

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val logLevel = HttpLoggingInterceptor.Level.BODY
        return HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message)
        }.setLevel(logLevel)
    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    private fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }

    suspend fun getPopularMovies(page: Int): Flow<List<MovieListing>> {
        return flow {
            val request = buildService(MovieApi::class.java)
            val call = request.getPopularMovies(page, API_KEY)
            var movies: List<MovieListing> = listOf()

            if (call.isSuccessful) {
                val maxPagesValue = call.body()?.total_pages
                if (maxPagesValue != null) {
                    maxPages = maxPagesValue.toInt()
                }
                if (call.body() != null) {
                    movies = call.body()!!.results
                }
            } else
                Log.d("RemoteDatasource", "Error with api call")
            emit(movies)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMovieDetails(id: Int): Flow<MovieDetails> {
        return flow {
            val request = buildService(MovieApi::class.java)
            val call = request.getMovieDetails(id, API_KEY)

            if (call.isSuccessful) {
                if (call.body() != null) {
                    emit(call.body()!!)
                }
            } else
                Log.d("RemoteDatasource", "Error with api call")
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCastList(id: Int): Flow<List<Cast>> {
        return flow {
            val request = buildService(MovieApi::class.java)
            val call = request.getCredits(id, API_KEY)

            if (call.isSuccessful) {
                if (call.body() != null) {
                    emit(call.body()!!.cast)
                }
            } else
                Log.d("RemoteDatasource", "Error with api call")
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCastImages(id: Int): Flow<List<Image>> {
        return flow {
            val request = buildService(MovieApi::class.java)
            val call = request.getCastImages(id, API_KEY)

            if (call.isSuccessful) {
                if (call.body() != null) {
                    emit(call.body()!!.profiles)
                }
            } else
                Log.d("RemoteDatasource", "Error with api call")
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMovieImages(id: Int): Flow<List<Image>> {
        return flow {
            val request = buildService(MovieApi::class.java)
            val call = request.getMovieImages(id, API_KEY)

            if (call.isSuccessful) {
                if (call.body() != null) {
                    emit(call.body()!!.posters)
                }
            } else
                Log.d("RemoteDatasource", "Error with api call")
        }.flowOn(Dispatchers.IO)
    }

    //TODO: cache in sharedprefs
    suspend fun getConfig(): Flow<ConfigurationResponse> {
        return flow {
            val request = buildService(MovieApi::class.java)
            val call = request.getConfig(API_KEY)

            if (call.isSuccessful) {
                if (call.body() != null) {
                    emit(call.body()!!)
                }
            } else
                Log.d("RemoteDatasource", "Error with api call")
        }.flowOn(Dispatchers.IO)
    }
}