package com.project.toko.domain.models.castModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class VoiceActor(
    @Expose @SerializedName("language") val language: String,
    @Expose @SerializedName("person") val person: Person
)