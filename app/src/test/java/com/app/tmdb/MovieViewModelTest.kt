package com.app.tmdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.tmdb.model.Movie
import com.app.tmdb.model.Movies
import com.app.tmdb.repository.MovieRepository
import com.app.tmdb.viewmodel.MovieViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MovieViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepository: MovieRepository

    private lateinit var viewModel: MovieViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieViewModel(mockRepository)
    }

    @Test
    fun testFetchPopularMovies() = runTest {
        val movie1 = Movie()
        val movie2 = Movie()

        val list:ArrayList<Movie> = ArrayList()
        list.add(movie1)
        list.add(movie2)
        val mockRepositories = Movies(1, list, null, null)

        Mockito.`when`(mockRepository.getPopularMovies()).thenReturn(mockRepositories)

        viewModel.refreshPopularMovies()

        testDispatcher.scheduler.advanceUntilIdle()
        val repositories = viewModel.moviesData.getOrAwaitValue()

        Assert.assertEquals(mockRepositories, repositories)
    }

    @After
    fun close() {
        Dispatchers.shutdown()
    }
}