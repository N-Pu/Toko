package com.example.animeapp.domain.exampleModels
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Prop(
     @SerializedName("from") val from: com.example.animeapp.domain.exampleModels.From,
     @SerializedName("string") val string: String,
     @SerializedName("to") val to: com.example.animeapp.domain.exampleModels.To
)