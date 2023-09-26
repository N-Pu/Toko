package com.project.toko.characterDetailedScreen.model.characterPicture


import com.google.gson.annotations.SerializedName

data class CharacterPicturesModel(

   @SerializedName("data") val data: List<Data>
)