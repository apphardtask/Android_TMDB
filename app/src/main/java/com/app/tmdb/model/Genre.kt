package com.app.tmdb.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Genre(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null
)