package com.app.tmdb.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Movies(
    @SerializedName("page")
    @Expose
    var page: Int? = null,
    @SerializedName("results")
    @Expose
    var movies: ArrayList<Movie>? = null,
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null,
    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null
)