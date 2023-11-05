package com.app.tmdb

import com.app.tmdb.model.Movie
import com.app.tmdb.model.Movies
import com.app.tmdb.repository.MovieRepository
import com.app.tmdb.retrofit.MoviesApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class MovieRepositoryTest {

    lateinit var movieRepository: MovieRepository

    @Mock
    lateinit var apiService: MoviesApiService

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        movieRepository = MovieRepository(apiService)
    }

    @Test
    fun `get popular movies test`() {
        runBlocking {
            Mockito.`when`(apiService.moviePopular()).thenReturn(Response.success(Movies()))
            val response = movieRepository.getPopularMovies()
            Assert.assertEquals(Movies(), response)
        }

    }

    @Test
    fun `get movie details test`() {
        runBlocking {
            Mockito.`when`(apiService.movieDetails(565770)).thenReturn(Response.success(Movie()))
            val response = movieRepository.getMovieDetails(565770)
            Assert.assertEquals(Movie(), response)
        }

    }

}