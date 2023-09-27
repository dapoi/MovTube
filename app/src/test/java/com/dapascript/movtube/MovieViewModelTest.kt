package com.dapascript.movtube

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dapascript.movtube.data.repo.MovieRepository
import com.dapascript.movtube.data.source.local.model.MovieEntity
import com.dapascript.movtube.presentation.adapter.MovieAdapter
import com.dapascript.movtube.presentation.viewmodel.MovieViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var movieRepository: MovieRepository

    @Test
    fun `when getMovies is called, then getMovies should be called`() = runTest {
        val dummyMovie = Dummy.listMovieDummy()
        val data: PagingData<MovieEntity> = MoviePagingSource.snapshot(dummyMovie)
        val expected = MutableLiveData<PagingData<MovieEntity>>()
        expected.value = data

        val repository = movieRepository.getMovies()
        Mockito.`when`(repository).thenReturn(flowOf(expected.value!!))

        val movieViewModel = MovieViewModel(movieRepository)
        val actual = movieViewModel.getMovies.asLiveData().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MovieAdapter.Companion,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actual)

        Assert.assertEquals(dummyMovie.size, differ.snapshot().size)
    }
}

class MoviePagingSource : PagingSource<Int, LiveData<List<MovieEntity>>>() {
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<MovieEntity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<MovieEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<MovieEntity>): PagingData<MovieEntity> {
            return PagingData.from(items)
        }
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}