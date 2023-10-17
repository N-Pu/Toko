package com.project.toko.detailScreen.model.castModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Webp(
    @Expose @SerializedName("image_url") val image_url: String,
    @Expose @SerializedName("small_image_url") val small_image_url: String
)