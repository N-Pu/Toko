package com.project.toko.detailScreen.model.recommendationsModel

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName
@Immutable
data class RecommendationsModel(
    @SerializedName("data") val data: List<RecommendationsData>
)