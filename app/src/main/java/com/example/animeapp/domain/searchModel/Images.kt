package com.example.animeapp.domain.searchModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Images(
    @Expose
    @SerializedName("jpg") val jpg: Jpg,
    val webp: Webp
)