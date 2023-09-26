package com.project.toko.staffDetailedScreen.model.personFullModel
import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("jpg") val jpg: Jpg,
    @SerializedName("webp") val webp: Webp
)