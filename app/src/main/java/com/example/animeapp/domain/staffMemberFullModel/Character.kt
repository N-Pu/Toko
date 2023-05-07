package com.example.animeapp.domain.staffMemberFullModel
import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("images") val images: ImagesXXXX,
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)