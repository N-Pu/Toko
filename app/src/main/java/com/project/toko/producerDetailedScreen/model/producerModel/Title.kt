package com.project.toko.producerDetailedScreen.model.producerModel
import com.google.gson.annotations.SerializedName

data class Title(
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String
)