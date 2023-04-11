package com.example.animeapp.domain.exampleModels
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Trailer(
     @SerializedName("embed_url") val embed_url: String,
     @SerializedName("url") val url: String,
     @SerializedName("youtube_id") val youtube_id: String
)