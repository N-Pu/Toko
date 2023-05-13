package com.example.animeapp.domain.models.detailModel


import com.google.gson.annotations.SerializedName

data class Aired(
    @SerializedName("from") val from: String,
    @SerializedName("prop") val prop: Prop,
    @SerializedName("to") val to: String
)