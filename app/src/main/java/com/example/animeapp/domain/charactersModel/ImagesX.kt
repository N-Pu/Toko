package com.example.animeapp.domain.charactersModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImagesX(
    @Expose @SerializedName("jpg") val jpg: JpgX
)