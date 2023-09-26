
package com.project.toko.characterDetailedScreen.model.characterFullModel
import com.google.gson.annotations.SerializedName


data class ImagesX(
    @SerializedName("jpg") val jpg: JpgX,
    @SerializedName("webp") val webp: WebpX
)