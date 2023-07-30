package com.project.toko.domain.models.characterModel
import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("images") val images: ImagesXXX,
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)