package com.dapascript.movtube.presentation.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapascript.movtube.databinding.FragmentMovieBinding
import com.dapascript.movtube.presentation.adapter.LoadingPageAdapter
import com.dapascript.movtube.presentation.adapter.MovieAdapter
import com.dapascript.movtube.presentation.ui.BaseFragment
import com.dapascript.movtube.presentation.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : BaseFragment<FragmentMovieBinding>(FragmentMovieBinding::inflate) {

    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        collectUiState()
    }

    private fun initAdapter() {
        // init adapter & set click listener
        movieAdapter = MovieAdapter(
            onClick = { model ->
                val id = model.id
                findNavController().navigate(
                    MovieFragmentDirections.actionMovieFragmentToDetailMovieFragment(id)
                )
            }
        )

        // init recyclerview
        binding.rvMovie.apply {
            val loadingPageAdapter = LoadingPageAdapter()
            adapter = movieAdapter.withLoadStateFooter(loadingPageAdapter)
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun collectUiState() {
        // collect data from viewmodel
        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.getMovies.collectLatest {
                movieAdapter.submitData(it)
            }
        }

        // collect load state
        viewLifecycleOwner.lifecycleScope.launch {
            movieAdapter.loadStateFlow.collect { loadState ->
                binding.apply {
                    progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                    rvMovie.isVisible = loadState.mediator?.refresh is LoadState.NotLoading
                    viewError.root.isVisible = loadState.mediator?.refresh is LoadState.Error

                    viewError.btnRefresh.setOnClickListener {
                        movieAdapter.retry()
                    }
                }
            }
        }
    }
}