package com.app.tmdb

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.tmdb.databinding.MovieDeatilsBinding
import com.app.tmdb.model.Movie
import com.app.tmdb.utils.Constants
import com.app.tmdb.utils.MyReachability
import com.app.tmdb.utils.getProgressDrawable
import com.app.tmdb.utils.getScreenHeight
import com.app.tmdb.utils.loadImage
import com.app.tmdb.viewmodel.MainViewModelFactory
import com.app.tmdb.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MovieViewModel
    lateinit var binding: MovieDeatilsBinding


    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory // Dagger will provide the object to this variable through field injection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MovieDeatilsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        (application as TMDBApplication).applicationComponent.inject(this)
        // what the above code do is, It will check the file for any of the @Inject property and if there are any
        // it will inject the correct object to them. Here it is mainViewModelFactory

        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MovieViewModel::class.java]

        lifecycleScope.launch {
            when {
                MyReachability.hasServerConnected(applicationContext) -> runOnUiThread {
                    //internet working
                    mainViewModel.refreshMovieDetails(intent.getIntExtra("movieId", 0))
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

        observeViewModel()
    }
    private fun observeViewModel() {

        mainViewModel.movieData.observe(this) { movie ->
            movie?.let {
                setData(movie)
            }
        }

        mainViewModel.loading.observe(this) { isLoading ->
            isLoading?.let {
                binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setData(movie: Movie){
        binding.ivMovie.layoutParams.height = (60* getScreenHeight(this) /100)
        with(movie){
            val progressDrawable = getProgressDrawable(binding.root.context)
            binding.ivMovie.loadImage(Constants.IMAGE_URL + posterPath, progressDrawable)
            binding.tvTitle.text = originalTitle + " (" + releaseDate?.split("-")?.get(0) + ")"
            var gen = ""
            for (g in genres ?: ArrayList()){
                gen = if(gen.isEmpty()) g?.name ?: "" else gen+", "+g?.name
            }
            binding.tvGenre.text = gen
            binding.tvVoteAverage.text = (voteAverage?.times(10)?.roundToInt()).toString() + "%"
            binding.pbVoteAverage.progress = voteAverage?.toInt() ?: 0
            binding.tvTagLine.text = tagline
            binding.tvDescription.text = overview
            binding.nestedScrollView.visibility = View.VISIBLE
        }
    }

}