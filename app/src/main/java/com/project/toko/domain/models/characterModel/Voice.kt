package com.project.toko.domain.models.characterModel
import com.google.gson.annotations.SerializedName


data class Voice(
    @SerializedName("language") val language: String,
    @SerializedName("person") val person: Person
)