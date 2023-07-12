package com.example.animeapp.domain.models.personFullModel
import com.google.gson.annotations.SerializedName

data class Voice(
    @SerializedName("anime") val anime:AnimeXX,
    @SerializedName("character") val character: Character,
    @SerializedName("role") val role: String
)