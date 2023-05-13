package com.example.animeapp.domain.models.detailModel

import com.google.gson.annotations.SerializedName
data class Studio(
     @SerializedName("mal_id") val mal_id: Int,
     @SerializedName("name") val name: String,
     @SerializedName("type") val type: String,
     @SerializedName("url") val url: String
)