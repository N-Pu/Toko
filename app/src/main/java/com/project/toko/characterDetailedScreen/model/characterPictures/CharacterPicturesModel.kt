package com.project.toko.characterDetailedScreen.model.characterPictures


import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class CharacterPicturesModel(

   @SerializedName("data") val data: List<CharacterPicturesData>
)