package com.project.toko.personDetailedScreen.model.personFullModel
import com.google.gson.annotations.SerializedName


data class Anime(
    @SerializedName("anime") val anime: AnimeX,
    @SerializedName("position") val position: String
)