package com.project.toko.detailScreen.model.recommendationsModel

import com.google.gson.annotations.SerializedName

data class RecommendationsModel(
    @SerializedName("data") val data: List<RecommendationsData>
)