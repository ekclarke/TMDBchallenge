package com.example.tmdbchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbchallenge.data.MovieDetails
import com.example.tmdbchallenge.data.Repository
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: Repository, val id: Int) : ViewModel() {
    private val _movieDetailsLiveData = MutableLiveData<MovieDetails>()
    val movieDetailsLiveData: LiveData<MovieDetails> = _movieDetailsLiveData

    init {
        viewModelScope.launch {
            repo.getMovieDetails(id)
                .collect {
                    _movieDetailsLiveData.postValue(it)
                }
        }
    }

}