package com.app.tmdb.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ProductionCompany(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("logo_path")
    @Expose
    var logoPath: String? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("origin_country")
    @Expose
    var originCountry: String? = null
)