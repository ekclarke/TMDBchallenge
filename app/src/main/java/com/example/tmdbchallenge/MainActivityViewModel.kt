package com.example.tmdbchallenge

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbchallenge.data.MovieListing
import com.example.tmdbchallenge.data.Repository
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val privateList = mutableListOf<MovieListing>()
    private val _movieListLiveData = MutableLiveData<List<MovieListing>>(privateList)
    val movieListLiveData: LiveData<List<MovieListing>> = _movieListLiveData

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _activeMovieId = MutableLiveData<Int>()
    val activeMovieId: LiveData<Int> = _activeMovieId

    private val _onlineLiveData = MutableLiveData<Boolean>()
    val onlineLiveData: LiveData<Boolean> = _onlineLiveData

    val repository = Repository()

    private var isBusy = false

    private var page = 1

    init {
        clearActiveMovie()
    }

    fun configure(activity: Activity) {
        viewModelScope.launch {
            repository.getConfig(activity)
        }
    }

    fun clearActiveMovie() {
        _activeMovieId.value = -1
    }

    fun updateActiveMovie(id: Int) {
        _activeMovieId.value = id
    }

    fun getMovieList(activity: Activity): Boolean {
        val limit = repository.getMaxPages()
        if ((page <= limit || limit == -1) && !isBusy) {
            isBusy = true
            _isLoading.value = true
            viewModelScope.launch {
                repository.getPopularMovies(page, activity)
                    .collect {
                        page++
                        privateList.addAll(it)
                        _movieListLiveData.postValue(privateList)
                        isBusy = false
                        _isLoading.value = false
                    }
            }
            return false
        } else if (page > limit) return true
        return false
    }

    fun isOnline(activity: Activity) {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                _onlineLiveData.value = true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                _onlineLiveData.value = true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                _onlineLiveData.value = true
            }
        } else _onlineLiveData.value = false
    }
}