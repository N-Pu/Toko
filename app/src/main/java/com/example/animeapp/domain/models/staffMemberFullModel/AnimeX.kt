package com.example.animeapp.domain.models.staffMemberFullModel
import com.google.gson.annotations.SerializedName

data class AnimeX(
    @SerializedName("images") val images: Images,
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String
)