package com.dapascript.movtube.presentation.ui.detail

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dapascript.movtube.R
import com.dapascript.movtube.data.source.local.model.MovieFavEntity
import com.dapascript.movtube.data.source.remote.model.VideoResponse
import com.dapascript.movtube.databinding.FragmentDetailMovieBinding
import com.dapascript.movtube.presentation.ui.BaseFragment
import com.dapascript.movtube.presentation.viewmodel.DetailViewModel
import com.dapascript.movtube.presentation.viewmodel.MovieFavViewModel
import com.dapascript.movtube.utils.Resource
import com.dapascript.movtube.utils.formatterDate
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMovieFragment :
    BaseFragment<FragmentDetailMovieBinding>(FragmentDetailMovieBinding::inflate) {

    private val detailViewModel: DetailViewModel by viewModels()
    private val movieFavViewModel: MovieFavViewModel by viewModels()

    private lateinit var youTubePlayer: YouTubePlayer

    private var isFullscreen = false
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                // if the player is in fullscreen, exit fullscreen
                youTubePlayer.toggleFullscreen()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // back button
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // fetch detail movie
        initViewModel()
    }

    private fun initViewModel() {
        detailViewModel.getDetailResult.observe(viewLifecycleOwner) {
            val (detail, video) = it
            binding.apply {
                val isLoading = detail is Resource.Loading && video is Resource.Loading
                val isError = detail is Resource.Error && video is Resource.Error
                val isSuccess = detail is Resource.Success && video is Resource.Success

                progressBar.isVisible = isLoading
                ytPlayer.isVisible = isSuccess
                clDetailMovie.isVisible = isSuccess
                viewError.root.isVisible = isError

                viewError.btnRefresh.setOnClickListener {
                    detailViewModel.fetchDetailMovie()
                }

                if (detail is Resource.Success) {
                    detail.data.let { detail ->
                        tvTitle.text = detail.title
                        tvOverview.text = detail.overview
                        tvReleaseDate.text = formatterDate(detail.releaseDate)
                    }

                    // fetch video
                    initTrailer(video)

                    // set fav id
                    movieFavViewModel.setFavorite(detail.data.id!!)
                }
            }
        }

        movieFavViewModel.isFavorite.observe(viewLifecycleOwner) { hasFavorite ->
            binding.fabFav.apply {
                // check state
                if (hasFavorite) {
                    setImageResource(R.drawable.ic_fav_true)
                } else {
                    setImageResource(R.drawable.ic_fav_false)
                }

                detailViewModel.getDetailResult.value?.first.let { data ->
                    val response = data as Resource.Success
                    val movieFav = MovieFavEntity(
                        id = response.data.id!!,
                        overview = response.data.overview,
                        title = response.data.title,
                        posterPath = response.data.posterPath,
                        date = response.data.releaseDate
                    )

                    setOnClickListener {
                        if (hasFavorite) movieFavViewModel.deleteMovieFavorite(movieFav)
                        else movieFavViewModel.insertMovieFavorite(movieFav)

                        val message = if (hasFavorite) "Removed from favorite"
                        else "Added to favorite"
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initTrailer(video: Resource<VideoResponse>) {
        if (video is Resource.Success) {
            val key = video.data.results?.first()?.key

            // set player options
            val iFramePlayerOptions = IFramePlayerOptions.Builder()
                .controls(1)
                .fullscreen(1)
                .build()

            binding.apply {
                lifecycle.addObserver(ytPlayer)
                ytPlayer.enableAutomaticInitialization = false

                // set fullscreen listener
                ytPlayer.addFullscreenListener(object : FullscreenListener {
                    override fun onEnterFullscreen(
                        fullscreenView: View,
                        exitFullscreen: () -> Unit
                    ) {
                        isFullscreen = true

                        ytPlayer.visibility = View.GONE
                        fullScreenViewContainer.visibility = View.VISIBLE
                        fullScreenViewContainer.addView(fullscreenView)

                        // request to landscape orientation
                        requireActivity().requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }

                    override fun onExitFullscreen() {
                        isFullscreen = false

                        ytPlayer.visibility = View.VISIBLE
                        fullScreenViewContainer.visibility = View.GONE
                        fullScreenViewContainer.removeAllViews()

                        // request to portrait orientation
                        requireActivity().requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    }
                })

                // set player listener
                val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        this@DetailMovieFragment.youTubePlayer = youTubePlayer
                        youTubePlayer.loadVideo(key!!, 0f)
                    }
                }

                // initialize player
                if (!::youTubePlayer.isInitialized) {
                    ytPlayer.initialize(listener, iFramePlayerOptions)
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.ytPlayer.release()
        onBackPressedCallback.remove()
        super.onDestroyView()
    }
}