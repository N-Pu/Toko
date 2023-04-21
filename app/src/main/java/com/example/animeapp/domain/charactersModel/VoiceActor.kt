package com.example.animeapp.domain.charactersModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class VoiceActor(
    @Expose @SerializedName("language") val language: String,
    @Expose @SerializedName("person") val person: Person
)