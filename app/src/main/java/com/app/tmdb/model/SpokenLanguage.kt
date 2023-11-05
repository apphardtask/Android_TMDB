package com.app.tmdb.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SpokenLanguage(
    @SerializedName("english_name")
    @Expose
    var englishName: String? = null,
    @SerializedName("iso_639_1")
    @Expose
    var iso6391: String? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null
)