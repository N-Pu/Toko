package com.project.toko.domain.models.detailModel


import com.project.toko.domain.models.newAnimeSearchModel.Data
import com.google.gson.annotations.SerializedName

data class AnimeDetailModel(
     @SerializedName("data") val data: Data

)