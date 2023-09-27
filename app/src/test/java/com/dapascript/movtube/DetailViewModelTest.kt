package com.dapascript.movtube

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.dapascript.movtube.data.repo.MovieRepository
import com.dapascript.movtube.data.source.remote.model.DetailResponse
import com.dapascript.movtube.data.source.remote.model.VideoResponse
import com.dapascript.movtube.presentation.viewmodel.DetailViewModel
import com.dapascript.movtube.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var movieRepository: MovieRepository

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        detailViewModel = DetailViewModel(movieRepository, savedStateHandle)
        detailViewModel.fetchDetailMovie()
    }

    @Test
    fun `when getMovieById is called, then getMovieById should be called`() = runTest {
        val detailResponse = DetailResponse()
        val videoResponse = VideoResponse()

        Mockito.`when`(movieRepository.getDetailMovies(1))
            .thenReturn(flowOf(Resource.Success(detailResponse)))
        Mockito.`when`(movieRepository.getVideo(1))
            .thenReturn(flowOf(Resource.Success(videoResponse)))

        detailViewModel.fetchDetailMovie()

        Assert.assertNotNull(
            Pair(
                Resource.Success(detailResponse),
                Resource.Success(videoResponse)
            )
        )
    }
}