package com.dapascript.movtube.presentation.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapascript.movtube.R
import com.dapascript.movtube.databinding.FragmentMovieFavoriteBinding
import com.dapascript.movtube.presentation.adapter.MovieFavAdapter
import com.dapascript.movtube.presentation.ui.BaseFragment
import com.dapascript.movtube.presentation.viewmodel.MovieFavViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFavoriteFragment :
    BaseFragment<FragmentMovieFavoriteBinding>(FragmentMovieFavoriteBinding::inflate) {

    private val movieFavViewModel: MovieFavViewModel by viewModels()
    private lateinit var movieFavAdapter: MovieFavAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initViewModel()
    }

    private fun initAdapter() {
        movieFavAdapter = MovieFavAdapter(
            onClick = {
                val id = it.id
                findNavController().navigate(
                    MovieFavoriteFragmentDirections.actionMovieFavoriteFragmentToDetailMovieFragment(
                        id
                    )
                )
            }
        )

        binding.rvMovieFavorite.apply {
            adapter = movieFavAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initViewModel() {
        movieFavViewModel.getMovieFavorite().observe(viewLifecycleOwner) { response ->
            binding.viewError.apply {
                root.isVisible = response.isNullOrEmpty()
                tvError.text = getString(R.string.empty_fav)
                btnRefresh.isVisible = false
            }

            movieFavAdapter.setMovieFav(response)
        }
    }
}