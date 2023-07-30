package com.project.toko.domain.models.castModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Character(
    @Expose @SerializedName("images") val images: Images,
    @Expose @SerializedName("mal_id") val mal_id: Int,
    @Expose @SerializedName("name") val name: String,
    @Expose @SerializedName("url") val url: String
)