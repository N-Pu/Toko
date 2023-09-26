package com.project.toko.staffDetailedScreen.model.personFullModel
import com.google.gson.annotations.SerializedName

data class Manga(
    @SerializedName("manga") val manga: MangaX,
    @SerializedName("position") val position: String
)