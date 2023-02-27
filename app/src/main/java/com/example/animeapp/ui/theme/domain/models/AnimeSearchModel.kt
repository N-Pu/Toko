package com.example.animeapp.ui.theme.domain.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnimeSearchModel(
    @Expose
    @SerializedName("data") val data : List<Data>,
    @Expose
    @SerializedName("pagination") val pagination: Pagination

)