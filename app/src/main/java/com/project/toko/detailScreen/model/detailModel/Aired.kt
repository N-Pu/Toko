
package com.project.toko.detailScreen.model.detailModel
import com.google.gson.annotations.SerializedName
data class Aired(
    @SerializedName("from") val from: String,
    @SerializedName("prop") val prop: Prop,
    @SerializedName("to") val to: String
)