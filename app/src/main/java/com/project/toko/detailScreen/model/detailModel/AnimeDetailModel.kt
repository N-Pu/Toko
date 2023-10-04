package com.project.toko.detailScreen.model.detailModel


import com.google.gson.annotations.SerializedName
import com.project.toko.homeScreen.model.newAnimeSearchModel.Data

data class AnimeDetailModel(
     @SerializedName("data") val data: Data
)