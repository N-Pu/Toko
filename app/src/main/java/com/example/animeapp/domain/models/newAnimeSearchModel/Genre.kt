package com.example.animeapp.domain.models.newAnimeSearchModel
import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String
)