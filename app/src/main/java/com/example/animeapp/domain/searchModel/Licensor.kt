package com.example.animeapp.domain.searchModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Licensor(
    @Expose
    @SerializedName("mal_id") val mal_id: Int,
    @Expose
    @SerializedName("name") val name: String,
    @Expose
    @SerializedName("type") val type: String,
    @Expose
    @SerializedName("url") val url: String
)