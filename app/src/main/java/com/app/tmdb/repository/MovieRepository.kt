package com.app.tmdb.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.tmdb.model.Movie
import com.app.tmdb.model.Movies
import com.app.tmdb.retrofit.MoviesApiService
import javax.inject.Inject

class MovieRepository @Inject constructor(private val moviesApi: MoviesApiService) {

    // suspend function to call the api
    suspend fun getPopularMovies() : Movies {

        val result = moviesApi.moviePopular()

        return result.body() ?: Movies()

    }

    // suspend function to call the api
    suspend fun getMovieDetails(movie_id: Int) : Movie {

        val result = moviesApi.movieDetails(movie_id)

        return result.body() ?: Movie()

    }

}

