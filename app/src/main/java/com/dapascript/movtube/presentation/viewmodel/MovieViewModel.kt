package com.dapascript.movtube.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dapascript.movtube.data.repo.MovieRepository
import com.dapascript.movtube.data.source.local.model.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _getMovies = MutableStateFlow<PagingData<MovieEntity>>(PagingData.empty())
    val getMovies = _getMovies.asStateFlow()

    init {
        viewModelScope.launch {
            movieRepository.getMovies().cachedIn(viewModelScope).collectLatest {
                _getMovies.value = it
            }
        }
    }
}