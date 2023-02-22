package com.example.tmdbchallenge

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

    val repository = Repository()

    private var isBusy = false

    private var page = 1

    fun getMovieList(): Boolean {
        val limit = repository.getMaxPages()
        if ((page <= limit || limit == -1) && !isBusy) {
            isBusy = true
            _isLoading.value = true
            viewModelScope.launch {
                repository.getPopularMovies(page)
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

    //TODO: fix redundancy
    fun getPosterUrl(): String {
        val url = ""
        viewModelScope.launch {
            repository.getConfig()
                .collect {
                   url.plus(it.images.secure_base_url)
                    url.plus(it.images.poster_sizes.first())
                }
        }
        return url
    }
}