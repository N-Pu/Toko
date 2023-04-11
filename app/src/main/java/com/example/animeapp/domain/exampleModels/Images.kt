package com.example.animeapp.domain.exampleModels
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Images(
     @SerializedName("jpg") val jpg: com.example.animeapp.domain.searchModel.Jpg,
     @SerializedName("webp") val webp: com.example.animeapp.domain.exampleModels.Webp
)