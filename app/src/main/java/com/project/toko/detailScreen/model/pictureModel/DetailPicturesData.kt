package com.project.toko.detailScreen.model.pictureModel

import com.google.gson.annotations.SerializedName

data class DetailPicturesData(
    @SerializedName("jpg") val jpg: Jpg,
    @SerializedName("webp") val webp: Webp
)