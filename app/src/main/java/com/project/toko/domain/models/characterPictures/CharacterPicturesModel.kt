package com.project.toko.domain.models.characterPictures


import com.google.gson.annotations.SerializedName

data class CharacterPicturesModel(

   @SerializedName("data") val data: List<Data>
)