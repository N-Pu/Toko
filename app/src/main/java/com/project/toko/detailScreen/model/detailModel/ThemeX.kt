
package com.project.toko.detailScreen.model.detailModel
import com.google.gson.annotations.SerializedName
data class ThemeX(
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String
)