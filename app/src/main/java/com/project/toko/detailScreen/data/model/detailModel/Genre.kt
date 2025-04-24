
package com.project.toko.detailScreen.data.model.detailModel
import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName
@Immutable
data class Genre(
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String
)