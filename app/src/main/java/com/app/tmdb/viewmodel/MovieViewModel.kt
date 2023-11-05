package com.app.tmdb.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.tmdb.model.Movie
import com.app.tmdb.model.Movies
import com.app.tmdb.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private val _moviesData = MutableLiveData<Movies>()
    val moviesData: LiveData<Movies> get() = _moviesData

    private val _movieData = MutableLiveData<Movie>()
    val movieData: LiveData<Movie> get() = _movieData

    val movieLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    // inti method will call right after the viewModel is created .
    // so all the codes inside the init method will be executed first
    // this is the coroutine that we use to connect the api.

    fun refreshPopularMovies() {
        loading.value = true
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        CoroutineScope(Dispatchers.Main).launch {
            val repos = repository.getPopularMovies()
            withContext(Dispatchers.Main) {
                _moviesData.value = repos
                loading.value = false
            }
        }
    }

    fun refreshMovieDetails(movie_id: Int) {
        loading.value = true
        fetchMovieDetails(movie_id)
    }

    private fun fetchMovieDetails(movie_id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val repos = repository.getMovieDetails(movie_id)
            withContext(Dispatchers.Main) {
                _movieData.value = repos
                loading.value = false
            }
        }
    }

}