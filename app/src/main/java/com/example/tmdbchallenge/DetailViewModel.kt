package com.example.tmdbchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbchallenge.data.Cast
import com.example.tmdbchallenge.data.Image
import com.example.tmdbchallenge.data.MovieDetails
import com.example.tmdbchallenge.data.Repository
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: Repository, val id: Int) : ViewModel() {

    private val _movieDetailsLiveData = MutableLiveData<MovieDetails>()
    val movieDetailsLiveData: LiveData<MovieDetails> = _movieDetailsLiveData

    private val _castListLiveData = MutableLiveData<List<Cast>>()
    val castListLiveData: LiveData<List<Cast>> = _castListLiveData

    private val _moviePostersLiveData = MutableLiveData<List<Image>>()
    val moviePostersLiveData: LiveData<List<Image>> = _moviePostersLiveData

    private val privateList = mutableListOf<Image?>()
    private val _castImagesLiveData = MutableLiveData<List<Image?>>(privateList)
    val castImagesLiveData: LiveData<List<Image?>> = _castImagesLiveData

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
                    val castIdList: List<Int> = it.map { cast -> cast.id }
                    privateList.clear()

                    for (id in castIdList) {
                        var castImage: Image? = null
                        repo.getCastImages(id)
                            .collect { list ->
                                castImage = list.firstOrNull()
                            }
                        privateList.add(castImage)
                    }
                    _castImagesLiveData.value = privateList
                }
        }
        viewModelScope.launch {
            repo.getMovieImages(id)
                .collect {
                    _moviePostersLiveData.postValue(it)
                }
        }
    }
}
