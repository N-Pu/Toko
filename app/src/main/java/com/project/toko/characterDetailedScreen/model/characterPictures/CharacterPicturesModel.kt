package com.project.toko.characterDetailedScreen.model.characterPictures


import com.google.gson.annotations.SerializedName

data class CharacterPicturesModel(

   @SerializedName("data") val data: List<CharacterPicturesData>
)