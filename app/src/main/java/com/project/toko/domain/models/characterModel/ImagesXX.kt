package com.project.toko.domain.models.characterModel
import com.google.gson.annotations.SerializedName


data class ImagesXX(
    @SerializedName("jpg") val jpg: Jpg,
    @SerializedName("webp") val webp: WebpX
)