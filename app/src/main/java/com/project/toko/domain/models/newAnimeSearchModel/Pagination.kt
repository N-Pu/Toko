package com.project.toko.domain.models.newAnimeSearchModel
import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("has_next_page") val has_next_page: Boolean,
    @SerializedName("items") val items: Items,
    @SerializedName("last_visible_page") val last_visible_page: Int
)