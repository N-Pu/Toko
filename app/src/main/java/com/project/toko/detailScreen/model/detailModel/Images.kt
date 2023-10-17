
package com.project.toko.detailScreen.model.detailModel
import com.google.gson.annotations.SerializedName
data class Images(
    @SerializedName("jpg") val jpg: Jpg,
    @SerializedName("webp") val webp: Webp
)