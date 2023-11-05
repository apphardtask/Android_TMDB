package com.app.tmdb.di

import android.content.Context
import android.net.ConnectivityManager
import com.app.tmdb.retrofit.MoviesApiService
import com.app.tmdb.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {

        val certificatePinner: CertificatePinner = CertificatePinner.Builder()
            .add(Constants.BASE_URL.replace("https://", ""), Constants.SHA256_1)
            .add(Constants.BASE_URL.replace("https://", ""), Constants.SHA256_2)
            .add(Constants.BASE_URL.replace("https://", ""), Constants.SHA256_3)
            .add(Constants.BASE_URL.replace("https://", ""), Constants.SHA256_4)
            .build()

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpBuilder = OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(Interceptor { chain ->
                var newRequest = chain.request().newBuilder()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", Constants.AuthorizationToken)
                    .build()
                chain.proceed(newRequest)
            })
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder().baseUrl(Constants.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpBuilder)
            .build()
    }

    @Singleton
    @Provides
    fun providesMoviesApi(retrofit: Retrofit): MoviesApiService {
        return retrofit.create(MoviesApiService::class.java)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}