package com.example.animeapp.domain.searchModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
//    @SerializedName("aired") val aired: Aired,
//    @SerializedName("background") val background: String,
    @Expose
    @SerializedName("episodes") val episodes: Int,
//    @SerializedName("genres") val genres: List<Genre>,
    @Expose
    @SerializedName("rating") val rating: String,
    @Expose
    @SerializedName("title") val title: String,

    @Expose
    @SerializedName("images") val images: com.example.animeapp.domain.exampleModels.Images,
//    @SerializedName("year") val year: Int
)
