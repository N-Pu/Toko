package com.project.toko.characterDetailedScreen.data.model.characterFullModel
import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName


@Immutable
data class Voice(
    @SerializedName("language") val language: String,
    @SerializedName("person") val person: Person
)