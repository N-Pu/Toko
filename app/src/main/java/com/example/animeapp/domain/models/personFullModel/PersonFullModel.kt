package com.example.animeapp.domain.models.personFullModel
import com.google.gson.annotations.SerializedName

data class PersonFullModel(
    @SerializedName("data") val data: Data
)