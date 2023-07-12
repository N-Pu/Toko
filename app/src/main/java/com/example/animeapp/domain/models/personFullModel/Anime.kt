package com.example.animeapp.domain.models.personFullModel
import com.google.gson.annotations.SerializedName


data class Anime(
    @SerializedName("anime") val anime: AnimeX,
    @SerializedName("position") val position: String
)