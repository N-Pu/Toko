package com.project.toko.domain.models.castModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class JpgX(
    @Expose @SerializedName("image_url") val image_url: String
)