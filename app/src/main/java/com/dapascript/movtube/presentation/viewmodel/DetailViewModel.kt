package com.dapascript.movtube.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapascript.movtube.data.repo.MovieRepository
import com.dapascript.movtube.data.source.remote.model.DetailResponse
import com.dapascript.movtube.data.source.remote.model.VideoResponse
import com.dapascript.movtube.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id = savedStateHandle.get<Int>("movieId")

    private val _getDetailResult =
        MutableLiveData<Pair<Resource<DetailResponse>, Resource<VideoResponse>>>()
    val getDetailResult: LiveData<Pair<Resource<DetailResponse>, Resource<VideoResponse>>> =
        _getDetailResult

    fun fetchDetailMovie() {
        viewModelScope.launch {
            movieRepository.apply {
                if (id != null) {
                    getDetailMovies(id).zip(getVideo(id)) { detail, video ->
                        detail to video
                    }.collect {
                        _getDetailResult.value = it
                    }
                }
            }
        }
    }

    init {
        fetchDetailMovie()
    }
}