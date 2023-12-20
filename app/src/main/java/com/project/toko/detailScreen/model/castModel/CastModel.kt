package com.project.toko.detailScreen.model.castModel

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Immutable
data class CastModel(

    @Expose @SerializedName("data") val data: List<CastData>
)