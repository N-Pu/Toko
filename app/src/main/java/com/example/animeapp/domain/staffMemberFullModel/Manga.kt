package com.example.animeapp.domain.staffMemberFullModel
import com.google.gson.annotations.SerializedName

data class Manga(
    @SerializedName("manga") val manga: MangaX,
    @SerializedName("position") val position: String
)