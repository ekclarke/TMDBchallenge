package com.example.tmdbchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbchallenge.data.*
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: Repository, val id: Int) : ViewModel() {
    private val _movieDetailsLiveData = MutableLiveData<MovieDetails>()
    val movieDetailsLiveData: LiveData<MovieDetails> = _movieDetailsLiveData

    private val _castListLiveData = MutableLiveData<List<Cast>>()
    val castListLiveData: LiveData<List<Cast>> = _castListLiveData

    private val _moviePostersLiveData = MutableLiveData<List<Image>>()
    val moviePostersLiveData: LiveData<List<Image>> = _moviePostersLiveData

    init {
        refreshData()
    }

    //TODO: streamline with await/async calls
    private fun refreshData() {
        viewModelScope.launch {
            repo.getMovieDetails(id)
                .collect {
                    _movieDetailsLiveData.postValue(it)
                }
        }
        viewModelScope.launch {
            repo.getCredits(id)
                .collect {
                    _castListLiveData.postValue(it)
                }
        }
        viewModelScope.launch {
            repo.getMovieImages(id)
                .collect {
                    _moviePostersLiveData.postValue(it)
                }
        }
    }

    fun getCastImage(id: Int): List<Image> {
        var castImageList = listOf<Image>()
        viewModelScope.launch {
            repo.getCastImages(id)
                .collect {
                    castImageList = it
                }
        }
        return castImageList
    }

    fun getBackdropUrl(): String {
        val url = ""
        viewModelScope.launch {
            repo.getConfig()
                .collect {
                    url.plus(it.images.secure_base_url)
                    url.plus(it.images.backdrop_sizes.first())
                }
        }
        return url
    }

    fun getProfileUrl(): String {
        val url = ""
        viewModelScope.launch {
            repo.getConfig()
                .collect {
                    url.plus(it.images.secure_base_url)
                    url.plus(it.images.profile_sizes.first())
                }
        }
        return url
    }

    //TODO: fix redundancy
    fun getPosterUrl(): String {
        val url = ""
        viewModelScope.launch {
            repo.getConfig()
                .collect {
                    url.plus(it.images.secure_base_url)
                    url.plus(it.images.profile_sizes.first())
                }
        }
        return url
    }
}