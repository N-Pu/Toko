package com.example.animeapp.domain.exampleModels
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class To(
     @SerializedName("day") val day: Int,
     @SerializedName("month") val month: Int,
     @SerializedName("year") val year: Int
)