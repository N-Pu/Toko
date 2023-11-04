package com.project.toko.personDetailedScreen.model.personPictures

import com.google.gson.annotations.SerializedName

data class PersonPicturesModel(
    @SerializedName("data") val data: List<PersonPicturesData>
)