package com.example.animeapp.domain.staffModel
import com.google.gson.annotations.SerializedName
data class Person(
     @SerializedName("images") val images: Images,
     @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("name") val name: String,
     @SerializedName("url") val url: String
)