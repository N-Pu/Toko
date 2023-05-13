package com.example.animeapp.domain.models.searchModel


import com.google.gson.annotations.SerializedName

data class AnimeSearchModel(
    
    @SerializedName("data") val data: List<Data>
)