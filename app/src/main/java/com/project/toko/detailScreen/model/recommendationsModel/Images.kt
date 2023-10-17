
package com.project.toko.detailScreen.model.recommendationsModel
import com.google.gson.annotations.SerializedName
data class Images(
    @SerializedName("jpg") val jpg: Jpg,
    @SerializedName("webp") val webp: Webp
)