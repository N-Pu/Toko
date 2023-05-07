package com.example.animeapp.domain.characterModel
import com.google.gson.annotations.SerializedName


data class CharacterFullModel(
    @SerializedName("data") val data: Data
)