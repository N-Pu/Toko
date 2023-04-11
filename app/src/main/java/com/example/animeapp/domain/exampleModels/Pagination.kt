package com.example.animeapp.domain.exampleModels
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Pagination(
     @SerializedName("has_next_page") val has_next_page: Boolean,
     @SerializedName("items") val items: com.example.animeapp.domain.exampleModels.Items,
     @SerializedName("last_visible_page") val last_visible_page: Int
)