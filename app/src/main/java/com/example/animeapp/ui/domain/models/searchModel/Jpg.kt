package com.example.animeapp.ui.domain.models.searchModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Jpg(
    @Expose
    @SerializedName("image_url") val image_url: String,
    val large_image_url: String,
    val small_image_url: String
)