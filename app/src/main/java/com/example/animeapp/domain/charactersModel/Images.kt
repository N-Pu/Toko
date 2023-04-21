package com.example.animeapp.domain.charactersModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Images(
    @Expose @SerializedName("jpg") val jpg: Jpg,
    @Expose @SerializedName("webp") val webp: Webp
)