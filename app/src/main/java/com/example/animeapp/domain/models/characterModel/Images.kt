package com.example.animeapp.domain.models.characterModel
import com.google.gson.annotations.SerializedName


data class Images(
    @SerializedName("jpg") val jpg: Jpg,
    @SerializedName("webp") val webp: Webp
)