package com.example.animeapp.domain.searchModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnimeSearchModel(
    
    @SerializedName("data") val data: List<Data>
)