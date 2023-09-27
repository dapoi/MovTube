package com.dapascript.movtube.presentation.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapascript.movtube.databinding.FragmentSearchBinding
import com.dapascript.movtube.presentation.MainActivity
import com.dapascript.movtube.presentation.adapter.SearchAdapter
import com.dapascript.movtube.presentation.ui.BaseFragment
import com.dapascript.movtube.presentation.viewmodel.SearchViewModel
import com.dapascript.movtube.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initViewModel()
    }

    private fun initAdapter() {
        searchAdapter = SearchAdapter(
            onClick = {
                val id = it.id.toString().toInt()
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToDetailMovieFragment(id)
                )
            }
        )

        binding.rvSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        searchViewModel.getSearch.observe(viewLifecycleOwner) { response ->
            binding.apply {
                progressBar.isVisible = response is Resource.Loading
                rvSearch.isVisible = response is Resource.Success
                viewError.root.isVisible = response is Resource.Error ||
                        (response is Resource.Success && response.data.isNullOrEmpty())
                viewError.tvError.text = response?.let {
                    when (it) {
                        is Resource.Error -> it.throwable.message
                        else -> "Data is empty"
                    }
                }
                viewError.btnRefresh.isVisible = response is Resource.Error
                viewError.btnRefresh.setOnClickListener {
                    initViewModel()
                }

                if (response is Resource.Success) {
                    searchAdapter.submitList(response.data)
                }
            }
        }

        (activity as MainActivity).setSearchMovieListener(
            object : MainActivity.SearchMovieListener {
                override fun searchMovie(query: String) {
                    searchViewModel.setSearch(query)
                }
            }
        )
    }
}