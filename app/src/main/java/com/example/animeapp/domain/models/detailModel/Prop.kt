package com.example.animeapp.domain.models.detailModel


import com.google.gson.annotations.SerializedName

data class Prop(
    @SerializedName("from") val from: From,
    @SerializedName("string") val string: String,
    @SerializedName("to") val to:To
)