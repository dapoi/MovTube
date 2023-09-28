package com.dapascript.movtube.presentation

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.dapascript.movtube.NavGraphDirections
import com.dapascript.movtube.R
import com.dapascript.movtube.databinding.ActivityMainBinding
import com.dapascript.movtube.presentation.viewmodel.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var searchView: SearchView

    private var searchMovieListener: SearchMovieListener? = null

    private val splashScreenViewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { splashScreenViewModel.keepRunning.value }
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
                setNavigationOnClickListener {
                    searchView.setQuery("", false)
                    navController.popBackStack()
                }

                // set padding
                if (destination.id == R.id.searchFragment) setPadding(0, 0, 20, 0)

                // state menu
                menu.findItem(R.id.action_search).isVisible = destination.id == R.id.movieFragment
                menu.findItem(R.id.action_search_expand).isVisible =
                    destination.id == R.id.searchFragment
                menu.findItem(R.id.action_fav).isVisible = destination.id == R.id.movieFragment

                // Get the search menu item
                searchView = menu.findItem(R.id.action_search_expand).actionView as SearchView
                searchView.queryHint = "Search Movie"
                searchView.isIconified = false
                searchView.setIconifiedByDefault(false)
                searchView.setBackgroundResource(R.drawable.bg_round)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        searchMovieListener?.searchMovie(newText.toString())
                        return false
                    }
                })

                // action menu click listener
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_fav -> {
                            navController.navigate(NavGraphDirections.actionGlobalMovieFavoriteFragment())
                            true
                        }

                        R.id.action_search -> {
                            navController.navigate(NavGraphDirections.actionGlobalSearchFragment())
                            true
                        }

                        else -> false
                    }
                }
            }
        }
    }

    fun setSearchMovieListener(listener: SearchMovieListener) {
        searchMovieListener = listener
    }

    interface SearchMovieListener {
        fun searchMovie(query: String)
    }
}