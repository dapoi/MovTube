package com.dapascript.movtube

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dapascript.movtube.data.repo.MovieRepository
import com.dapascript.movtube.data.source.remote.model.ResultsItem
import com.dapascript.movtube.presentation.viewmodel.SearchViewModel
import com.dapascript.movtube.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
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
class SearchViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setUp() {
        searchViewModel = SearchViewModel(movieRepository)
        searchViewModel.setSearch("")
    }

    @Test
    fun `when search is called, then search should be called`() = runBlocking {
        val observer = Observer<Resource<List<ResultsItem?>?>> {}
        try {
            val expected = MutableLiveData<Resource<List<ResultsItem>>>()
            expected.value = Resource.Success(listOf())

            Mockito.`when`(movieRepository.searchMovie(""))
                .thenReturn(flowOf(Resource.Success(listOf())))

            val actual = searchViewModel.getSearch.observeForever(observer)
            Assert.assertNotNull(actual)
        } finally {
            searchViewModel.getSearch.removeObserver(observer)
        }
    }
}