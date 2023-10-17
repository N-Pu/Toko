
package com.project.toko.detailScreen.model.recommendationsModel
import com.google.gson.annotations.SerializedName
data class Entry(
    @SerializedName("images") val images: Images,
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String
)