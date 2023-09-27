package com.dapascript.movtube.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dapascript.movtube.data.source.local.model.MovieFavEntity
import com.dapascript.movtube.databinding.ItemMovieBinding
import com.dapascript.movtube.utils.formatterDate
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class MovieFavAdapter(
    private val onClick: (MovieFavEntity) -> Unit,
) : RecyclerView.Adapter<MovieFavAdapter.MovieFavViewHolder>() {

    private val listMovieFav = mutableListOf<MovieFavEntity>()

    fun setMovieFav(movieFav: List<MovieFavEntity>) {
        listMovieFav.clear()
        listMovieFav.addAll(movieFav)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieFavViewHolder {
        return MovieFavViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieFavViewHolder, position: Int) {
        holder.bind(listMovieFav[position])
    }

    override fun getItemCount(): Int = listMovieFav.size

    inner class MovieFavViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieFavEntity) {
            binding.apply {
                binding.apply {
                    tvMovieTitle.text = movie.title
                    tvOverview.text = movie.overview
                    tvDate.text = formatterDate(movie.date)

                    val imageShimmer =
                        Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                            .setDuration(1000) // how long the shimmering animation takes to do one full sweep
                            .setBaseAlpha(0.7f) //the alpha of the underlying children
                            .setHighlightAlpha(0.6f) // the shimmer alpha amount
                            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                            .setAutoStart(true)
                            .build()

                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                        .placeholder(ShimmerDrawable().apply { setShimmer(imageShimmer) })
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivMoviePoster)
                }
            }
        }

        init {
            binding.root.setOnClickListener {
                onClick(listMovieFav[bindingAdapterPosition])
            }
        }
    }
}