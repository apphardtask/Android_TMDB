package com.app.tmdb.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object MyReachability {

    private suspend fun hasNetworkAvailable(context: Context): Boolean {
        return withContext(Dispatchers.IO){

            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return@withContext false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return@withContext false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return@withContext result
        }
    }

    suspend fun hasInternetConnected(context: Context): Boolean {
        return withContext(Dispatchers.IO){

            if (hasNetworkAvailable(context)) {
                try {
                    val connection = URL("https://www.google.com").openConnection() as HttpURLConnection
                    connection.setRequestProperty("User-Agent", "Test")
                    connection.setRequestProperty("Connection", "close")
                    connection.connectTimeout = 1500 // configurable
                    connection.connect()
                    Log.d("classTag", "hasInternetConnected: ${(connection.responseCode == 200)}")
                    return@withContext (connection.responseCode == 200)
                } catch (e: IOException) {
                    Log.e("classTag", "Error checking internet connection", e)
                }
            } else {
                Log.w("classTag", "No network available!")
            }
            Log.d("classTag", "hasInternetConnected: false")
            return@withContext false
        }
    }

    suspend fun hasServerConnected(context: Context): Boolean {
        return withContext(Dispatchers.IO){

            if (hasNetworkAvailable(context)) {
                try {
                    val connection = URL("https://api.themoviedb.org").openConnection() as HttpURLConnection
                    connection.setRequestProperty("User-Agent", "Test")
                    connection.setRequestProperty("Connection", "close")
                    connection.connectTimeout = 1500 // configurable
                    connection.connect()
                    Log.d("classTag", "hasServerConnected: ${(connection.responseCode == 200)}")
                    return@withContext (connection.responseCode == 200)
                } catch (e: IOException) {
                    Log.e("classTag", "Error checking internet connection", e)
                }
            } else {
                Log.w("classTag", "Server is unavailable!")
            }
            Log.d("classTag", "hasServerConnected: false")
            return@withContext false
        }
    }

}