package com.project.toko.characterDetailedScreen.data.model.characterFullModel

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class Anime(
    @SerializedName("anime") val anime: AnimeX,
    @SerializedName("role") val role: String
)