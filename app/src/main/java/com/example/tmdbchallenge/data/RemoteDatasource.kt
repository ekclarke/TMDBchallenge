package com.example.tmdbchallenge.data

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.tmdbchallenge.R
import com.example.tmdbchallenge.utilities.ToastHelper
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

    suspend fun getPopularMovies(page: Int, context: Context): Flow<List<MovieListing>> {
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
                ToastHelper.showErrorToast(context, "movies")
            emit(movies)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMovieDetails(id: Int, context: Context): Flow<MovieDetails> {
        return flow {
            val request = buildService(MovieApi::class.java)
            val call = request.getMovieDetails(id, API_KEY)

            if (call.isSuccessful) {
                if (call.body() != null) {
                    emit(call.body()!!)
                }
            } else
                ToastHelper.showErrorToast(context, "movie details")
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCastList(id: Int, context: Context): Flow<List<Cast>> {
        return flow {
            val request = buildService(MovieApi::class.java)
            val call = request.getCredits(id, API_KEY)

            if (call.isSuccessful) {
                if (call.body() != null) {
                    emit(call.body()!!.cast)
                }
            } else
                ToastHelper.showErrorToast(context, "cast list")
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCastImages(id: Int, context: Context): Flow<List<Image>> {
        return flow {
            val request = buildService(MovieApi::class.java)
            val call = request.getCastImages(id, API_KEY)

            if (call.isSuccessful) {
                if (call.body() != null) {
                    emit(call.body()!!.profiles)
                }
            } else
                ToastHelper.showErrorToast(context, "cast images")

        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMovieImages(id: Int, context: Context): Flow<List<Image>> {
        return flow {
            val request = buildService(MovieApi::class.java)
            val call = request.getMovieImages(id, API_KEY)

            if (call.isSuccessful) {
                if (call.body() != null) {
                    emit(call.body()!!.posters)
                }
            } else
                ToastHelper.showErrorToast(context, "movie images")

        }.flowOn(Dispatchers.IO)
    }

    suspend fun getConfig(activity: Activity?): Flow<ConfigurationResponse> {
        val sharedPref =
            activity?.getSharedPreferences(
                activity.getString(R.string.prefs_key),
                Context.MODE_PRIVATE
            )
        return flow {
            val request = buildService(MovieApi::class.java)
            val call = request.getConfig(API_KEY)

            if (call.isSuccessful) {
                if (call.body() != null) {
                    val config = call.body()!!.images
                    if (sharedPref != null) {
                        with(sharedPref.edit()) {
                            putString(
                                activity.getString(R.string.base_url_key),
                                config.base_url
                            )
                            putString(
                                activity.getString(R.string.secure_base_url_key),
                                config.secure_base_url
                            )
                            putStringSet(
                                activity.getString(R.string.backdrop_sizes_key),
                                config.backdrop_sizes.toMutableSet()
                            )
                            putStringSet(
                                activity.getString(R.string.poster_sizes_key),
                                config.poster_sizes.toMutableSet()
                            )
                            putStringSet(
                                activity.getString(R.string.profile_sizes_key),
                                config.profile_sizes.toMutableSet()
                            )
                            apply()
                        }
                    }
                    emit(call.body()!!)
                }
            } else
                activity?.applicationContext?.let {
                    ToastHelper.showErrorToast(
                        it,
                        "image configuration"
                    )
                }
        }.flowOn(Dispatchers.IO)
    }
}