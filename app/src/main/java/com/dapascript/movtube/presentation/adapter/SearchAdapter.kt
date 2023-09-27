package com.dapascript.movtube.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dapascript.movtube.data.source.remote.model.ResultsItem
import com.dapascript.movtube.databinding.ItemMovieBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class SearchAdapter(
    private val onClick: (ResultsItem) -> Unit
) : ListAdapter<ResultsItem, SearchAdapter.SearchViewHolder>(SearchAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: ResultsItem) {
            binding.apply {
                tvMovieTitle.text = movie.title
                tvOverview.text = movie.overview
                tvDate.text = movie.release_date

                val imageShimmer =
                    Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                        .setDuration(1000) // how long the shimmering animation takes to do one full sweep
                        .setBaseAlpha(0.7f) //the alpha of the underlying children
                        .setHighlightAlpha(0.6f) // the shimmer alpha amount
                        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                        .setAutoStart(true)
                        .build()

                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                    .placeholder(ShimmerDrawable().apply { setShimmer(imageShimmer) })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivMoviePoster)
            }
        }

        init {
            binding.root.setOnClickListener {
                onClick(getItem(bindingAdapterPosition))
            }
        }
    }

    companion object : DiffUtil.ItemCallback<ResultsItem>() {
        override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
            return oldItem == newItem
        }
    }
}