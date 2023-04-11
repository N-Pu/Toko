package com.example.animeapp.domain.exampleModels
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Broadcast(
     @SerializedName("day") val day: String,
     @SerializedName("string") val string: String,
     @SerializedName("time") val time: String,
     @SerializedName("timezone") val timezone: String
)