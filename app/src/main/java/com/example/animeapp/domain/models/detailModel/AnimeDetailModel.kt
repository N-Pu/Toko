package com.example.animeapp.domain.models.detailModel


import com.example.animeapp.domain.models.newAnimeSearchModel.Data
import com.google.gson.annotations.SerializedName

data class AnimeDetailModel(
     @SerializedName("data") val data: Data

)