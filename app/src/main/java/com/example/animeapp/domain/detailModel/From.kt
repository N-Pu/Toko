package com.example.animeapp.domain.detailModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class From(
     @SerializedName("day") val day: Int,
     @SerializedName("month") val month: Int,
     @SerializedName("year") val year: Int
)