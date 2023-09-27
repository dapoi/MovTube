package com.dapascript.movtube.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dapascript.movtube.data.repo.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {

    val getMovies = movieRepository.getMovies().cachedIn(viewModelScope)
}