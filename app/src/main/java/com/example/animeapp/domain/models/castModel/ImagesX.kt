package com.example.animeapp.domain.models.castModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImagesX(
    @Expose @SerializedName("jpg") val jpg: JpgX
)