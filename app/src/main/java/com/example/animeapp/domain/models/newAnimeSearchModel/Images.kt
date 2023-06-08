package com.example.animeapp.domain.models.newAnimeSearchModel
import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("jpg") val jpg: Jpg,
    @SerializedName("webp") val webp: Webp
)