package com.project.toko.detailScreen.model.detailModel


import com.google.gson.annotations.SerializedName

data class AnimeDetailModel(
     @SerializedName("data") val data: com.project.toko.homeScreen.model.newAnimeSearchModel.Data
)