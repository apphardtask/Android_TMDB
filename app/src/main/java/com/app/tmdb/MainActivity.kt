package com.app.tmdb

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.tmdb.adapters.MovieListAdapter
import com.app.tmdb.adapters.OnClickListener
import com.app.tmdb.databinding.ActivityMainBinding
import com.app.tmdb.model.Movie
import com.app.tmdb.utils.MyReachability
import com.app.tmdb.viewmodel.MainViewModelFactory
import com.app.tmdb.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MovieViewModel
    private lateinit var moviesList: ArrayList<Movie?>
    private lateinit var moviesAdapter: MovieListAdapter
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory // Dagger will provide the object to this variable through field injection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        (application as TMDBApplication).applicationComponent.inject(this)
        // what the above code do is, It will check the file for any of the @Inject property and if there are any
        // it will inject the correct object to them. Here it is mainViewModelFactory

        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MovieViewModel::class.java]

        moviesList = arrayListOf()

        lifecycleScope.launch {
            when {
                MyReachability.hasServerConnected(applicationContext) -> runOnUiThread {
                    //internet working
                    mainViewModel.refreshPopularMovies()
                    binding.swipeRefreshLayout.setOnRefreshListener {
                        binding.swipeRefreshLayout.isRefreshing = false
                        mainViewModel.refreshPopularMovies()
                    }
                }
                MyReachability.hasInternetConnected(applicationContext) -> runOnUiThread {
                    Snackbar.make(binding.root, getString(R.string.NoInternet), Snackbar.LENGTH_LONG)
                        .show()
                    binding.loadingView.visibility = View.GONE
                }
                else -> runOnUiThread {
                    Snackbar.make(binding.root, getString(R.string.NoInternet), Snackbar.LENGTH_LONG)
                        .show()
                    binding.loadingView.visibility = View.GONE
                }
            }
        }

        moviesAdapter = MovieListAdapter(this, arrayListOf(), OnClickListener { item ->
            val intent = Intent(this, MovieDetailsActivity::class.java)
            intent.putExtra("movieId", item?.id )
            startActivity(intent)
        })

        binding.moviesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moviesAdapter
        }

        observeViewModel()

    }

    private fun observeViewModel() {

        mainViewModel.moviesData.observe(this) { movies ->
            movies?.let {
                binding.moviesList.visibility = View.VISIBLE
                moviesList.addAll(it.movies?.take(10) ?: ArrayList())
                moviesAdapter.updateMovies(ArrayList(it.movies?.take(10)))
            }
        }
        mainViewModel.movieLoadError.observe(this) { isError ->
            isError?.let { binding.listError.visibility = if (it) {View.VISIBLE} else View.GONE }
        }

        mainViewModel.loading.observe(this) { isLoading ->
            isLoading?.let {
                binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.listError.visibility = View.GONE
                    binding.moviesList.visibility = View.GONE
                }
            }
        }

    }

}