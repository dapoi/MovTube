package com.dapascript.movtube.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.dapascript.movtube.NavGraphDirections
import com.dapascript.movtube.R
import com.dapascript.movtube.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNav()
        setUpToolbar()
    }

    private fun setUpNav() {
        navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                // set toolbar title
                title = when (destination.id) {
                    R.id.movieFragment -> getString(R.string.app_name)
                    R.id.detailMovieFragment -> getString(R.string.detail_movie)
                    R.id.movieFavoriteFragment -> getString(R.string.favorite)
                    else -> getString(R.string.app_name)
                }

                // disable nav icon in movie fragment
                navigationIcon = when (destination.id) {
                    R.id.movieFragment -> null
                    else -> ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_arrow_back
                    )
                }

                // navIcon click listener
                setNavigationOnClickListener { navController.popBackStack() }

                // disable menu except movie fragment
                menu.findItem(R.id.action_search).isVisible = destination.id == R.id.movieFragment
                menu.findItem(R.id.action_fav).isVisible = destination.id == R.id.movieFragment

                // action menu click listener
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_fav -> {
                            navController.navigate(NavGraphDirections.actionGlobalMovieFavoriteFragment())
                            true
                        }

                        else -> false
                    }
                }
            }
        }
    }
}