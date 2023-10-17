package com.project.toko.randomAnimeScreen.model


import com.google.gson.annotations.SerializedName
import com.project.toko.homeScreen.model.newAnimeSearchModel.AnimeSearchData

data class AnimeRandomModel(
     @SerializedName("data") val data: AnimeSearchData
)