package com.example.animeapp.domain.models.searchModel


import com.google.gson.annotations.SerializedName

data class Jpg(
    
    @SerializedName("image_url") val image_url: String,
//    val large_image_url: String,
//    val small_image_url: String
)