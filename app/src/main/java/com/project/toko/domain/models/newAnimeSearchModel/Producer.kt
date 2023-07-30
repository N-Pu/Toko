package com.project.toko.domain.models.newAnimeSearchModel
import com.google.gson.annotations.SerializedName


data class Producer(
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String
)