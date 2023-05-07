package com.example.animeapp.domain.characterModel
import com.google.gson.annotations.SerializedName


data class WebpX(
    @SerializedName("image_url") val image_url: String,
    @SerializedName("small_image_url") val small_image_url: String
)