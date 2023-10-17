
package com.project.toko.detailScreen.model.detailModel
import com.google.gson.annotations.SerializedName
data class Streaming(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)