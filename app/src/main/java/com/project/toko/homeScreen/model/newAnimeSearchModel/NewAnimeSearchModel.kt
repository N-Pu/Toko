package com.project.toko.homeScreen.model.newAnimeSearchModel
import com.google.gson.annotations.SerializedName

data class NewAnimeSearchModel(
    @SerializedName("data") val data: List<Data>,
    @SerializedName("pagination") val pagination: Pagination
)