package com.example.animeapp.domain.models.detailModel

import com.google.gson.annotations.SerializedName
data class Title(
     @SerializedName("title") val title: String,
     @SerializedName("type") val type: String
)