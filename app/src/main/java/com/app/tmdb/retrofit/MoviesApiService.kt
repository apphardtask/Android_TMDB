package com.app.tmdb.retrofit

import com.app.tmdb.model.Movie
import com.app.tmdb.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesApiService {
    @GET("movie/popular?language=en-US&page=1")
    suspend fun moviePopular(): Response<Movies> // Using coroutines here so making the function suspend.

    @GET("movie/{movie_id}?language=en-US")
    suspend fun movieDetails(@Path("movie_id") movie_id: Int): Response<Movie> // Using coroutines here so making the function suspend.
}