package com.example.animeapp.domain.exampleModels
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Aired(
     @SerializedName("from") val from: String,
     @SerializedName("prop") val prop: com.example.animeapp.domain.exampleModels.Prop,
     @SerializedName("to") val to: String
)