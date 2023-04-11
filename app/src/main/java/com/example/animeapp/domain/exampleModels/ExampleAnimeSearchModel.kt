package com.example.animeapp.domain.exampleModels
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class ExampleAnimeSearchModel(
    val `data`: List<com.example.animeapp.domain.exampleModels.Data>,
     @SerializedName("pagination") val pagination: com.example.animeapp.domain.exampleModels.Pagination
)