package com.example.animeapp.domain.models.characterModel

import com.google.gson.annotations.SerializedName


data class Anime(
    @SerializedName("anime") val anime: AnimeX,
    @SerializedName("role") val role: String
)