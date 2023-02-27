package com.example.animeapp.ui.theme.domain.models

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