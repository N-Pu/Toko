package com.example.animeapp.domain.detailModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ExplicitGenre(
     @SerializedName("mal_id") val mal_id: Int,
     @SerializedName("name") val name: String,
     @SerializedName("type") val type: String,
     @SerializedName("url") val url: String
)