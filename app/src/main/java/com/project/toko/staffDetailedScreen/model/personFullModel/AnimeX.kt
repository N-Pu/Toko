package com.project.toko.staffDetailedScreen.model.personFullModel
import com.google.gson.annotations.SerializedName

data class AnimeX(
    @SerializedName("images") val images: Images,
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String
)