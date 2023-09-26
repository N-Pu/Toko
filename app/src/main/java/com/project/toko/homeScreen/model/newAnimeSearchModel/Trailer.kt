package com.project.toko.homeScreen.model.newAnimeSearchModel
import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("embed_url") val embed_url: String,
    @SerializedName("url") val url: String,
    @SerializedName("youtube_id") val youtube_id: String
)