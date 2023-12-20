
package com.project.toko.detailScreen.model.detailModel
import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName
@Immutable
data class Relation(
    @SerializedName("entry") val entry: List<Entry>,
    @SerializedName("relation") val relation: String
)