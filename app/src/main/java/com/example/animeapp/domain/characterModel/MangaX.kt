package com.example.animeapp.domain.characterModel
import com.google.gson.annotations.SerializedName


data class MangaX(
    @SerializedName("images") val images: ImagesXX,
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String
)