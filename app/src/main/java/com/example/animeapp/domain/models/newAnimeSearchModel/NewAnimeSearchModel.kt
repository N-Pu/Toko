package com.example.animeapp.domain.models.newAnimeSearchModel
import com.google.gson.annotations.SerializedName

data class NewAnimeSearchModel(
    @SerializedName("data") val data: List<Data>,
    @SerializedName("pagination") val pagination: Pagination
)