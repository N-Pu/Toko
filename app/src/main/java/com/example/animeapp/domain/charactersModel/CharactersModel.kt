package com.example.animeapp.domain.charactersModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CharactersModel(

    @Expose @SerializedName("data") val data: List<Data>
)