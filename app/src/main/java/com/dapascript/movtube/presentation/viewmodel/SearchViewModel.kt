package com.dapascript.movtube.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapascript.movtube.data.repo.MovieRepository
import com.dapascript.movtube.data.source.remote.model.ResultsItem
import com.dapascript.movtube.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _getSearch = MutableLiveData<Resource<List<ResultsItem?>?>>()
    val getSearch: LiveData<Resource<List<ResultsItem?>?>> = _getSearch

    private var _searchMovie: Job? = null

    fun setSearch(query: String) {
        _searchMovie?.cancel()
        if (query.isBlank()) return
        _searchMovie = viewModelScope.launch {
            delay(1000)
            movieRepository.searchMovie(query).collect { result ->
                _getSearch.value = result
            }
        }
    }
}