package com.example.animeapp.domain.exampleModels
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Title(
     @SerializedName("title") val title: String,
     @SerializedName("type") val type: String
)