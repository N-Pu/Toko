package com.example.animeapp.ui.domain.models.searchModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Pagination(
    @Expose
    @SerializedName("has_next_page") val has_next_page: Boolean,
    @Expose
    @SerializedName("items") val items: Items,
    @Expose
    @SerializedName("last_visible_page") val last_visible_page: Int
)