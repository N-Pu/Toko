package com.project.toko.domain.models.characterModel
import com.google.gson.annotations.SerializedName


data class Manga(
    @SerializedName("manga") val manga: MangaX,
    @SerializedName("role") val role: String
)