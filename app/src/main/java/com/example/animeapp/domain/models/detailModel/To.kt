package com.example.animeapp.domain.models.detailModel

import com.google.gson.annotations.SerializedName
data class To(
     @SerializedName("day") val day: Int,
     @SerializedName("month") val month: Int,
     @SerializedName("year") val year: Int
)