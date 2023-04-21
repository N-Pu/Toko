package com.example.animeapp.domain.charactersModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class JpgX(
    @Expose @SerializedName("image_url") val image_url: String
)