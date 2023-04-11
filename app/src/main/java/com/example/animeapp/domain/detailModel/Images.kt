package com.example.animeapp.domain.detailModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Images(
     @SerializedName("jpg") val jpg: Jpg,
     @SerializedName("webp") val webp: Webp
)