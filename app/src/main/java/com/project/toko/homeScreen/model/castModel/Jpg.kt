package com.project.toko.homeScreen.model.castModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Jpg(
    @Expose @SerializedName("image_url") val image_url: String,
    @Expose @SerializedName("small_image_url") val small_image_url: String
)