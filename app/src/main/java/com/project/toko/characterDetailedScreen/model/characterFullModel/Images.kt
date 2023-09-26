package com.project.toko.characterDetailedScreen.model.characterFullModel
import com.google.gson.annotations.SerializedName


data class Images(
    @SerializedName("jpg") val jpg: Jpg,
    @SerializedName("webp") val webp: Webp
)