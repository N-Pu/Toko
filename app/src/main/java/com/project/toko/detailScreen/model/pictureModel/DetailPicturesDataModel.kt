package com.project.toko.detailScreen.model.pictureModel

import com.google.gson.annotations.SerializedName

data class DetailPicturesDataModel(
    @SerializedName("data") val data: List<DetailPicturesData>
)