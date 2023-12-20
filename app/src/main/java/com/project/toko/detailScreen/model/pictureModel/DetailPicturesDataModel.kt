package com.project.toko.detailScreen.model.pictureModel

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName
@Immutable
data class DetailPicturesDataModel(
    @SerializedName("data") val data: List<DetailPicturesData>
)