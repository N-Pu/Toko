package com.example.animeapp.domain.detailModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnimeDetailModel(
     @SerializedName("data") val data: Data

)