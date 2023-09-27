package com.dapascript.movtube.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dapascript.movtube.data.repo.MovieRepository
import com.dapascript.movtube.data.source.local.model.MovieFavEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieFavViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun insertMovieFavorite(movieFav: MovieFavEntity) {
        viewModelScope.launch {
            movieRepository.insertMovieToFavorite(movieFav)
        }
    }

    fun deleteMovieFavorite(movieFav: MovieFavEntity) {
        viewModelScope.launch {
            movieRepository.deleteMovieFromFavorite(movieFav)
        }
    }

    fun getMovieFavorite() = movieRepository.getFavorite().asLiveData()

    fun setFavorite(id: Int) {
        viewModelScope.launch {
            delay(200)
            movieRepository.isFavorite(id).collect {
                _isFavorite.value = it
            }
        }
    }
}