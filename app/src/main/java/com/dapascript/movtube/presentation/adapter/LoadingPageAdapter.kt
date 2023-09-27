package com.dapascript.movtube.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dapascript.movtube.databinding.ItemLoadingPageBinding

class LoadingPageAdapter : LoadStateAdapter<LoadingPageAdapter.LoadingPageViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingPageViewHolder {
        return LoadingPageViewHolder(
            ItemLoadingPageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LoadingPageViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadingPageViewHolder(
        private val binding: ItemLoadingPageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
        }
    }
}