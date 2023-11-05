package com.app.tmdb

import com.app.tmdb.model.Movies
import com.app.tmdb.retrofit.MoviesApiService
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MoviesApiTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var apiService: MoviesApiService
    lateinit var gson: Gson

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        gson = Gson()
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(MoviesApiService::class.java)
    }


    @Test
    fun `get all movie api test`() {
        runBlocking {
            val mockResponse = MockResponse()
            mockWebServer.enqueue(mockResponse.setBody("{}"))
            val response = apiService.moviePopular()
            val request = mockWebServer.takeRequest()
            Assert.assertEquals("/movie/popular?language=en-US&page=1", request.path)
            Assert.assertEquals(Movies(), response.body())
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

}