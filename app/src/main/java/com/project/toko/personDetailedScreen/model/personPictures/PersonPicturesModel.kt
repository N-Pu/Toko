package com.project.toko.personDetailedScreen.model.personPictures

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class PersonPicturesModel(
    @SerializedName("data") val data: List<PersonPicturesData>
)