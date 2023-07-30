package com.project.toko.domain.models.newAnimeSearchModel
import com.google.gson.annotations.SerializedName

data class Items(
    @SerializedName("count") val count: Int,
    @SerializedName("per_page") val per_page: Int,
    @SerializedName("total") val total: Int
)