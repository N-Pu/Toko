package com.example.animeapp.domain.models.castModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
    @Expose @SerializedName("character") val character: Character,
    @Expose @SerializedName("role") val role: String,
    @Expose @SerializedName("voice_actors") val voice_actors: List<VoiceActor>
)