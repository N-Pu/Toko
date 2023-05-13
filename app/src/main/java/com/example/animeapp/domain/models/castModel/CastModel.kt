package com.example.animeapp.domain.models.castModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CastModel(

    @Expose @SerializedName("data") val data: List<Data>
)