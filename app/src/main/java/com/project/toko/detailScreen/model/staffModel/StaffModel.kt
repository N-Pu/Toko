package com.project.toko.detailScreen.model.staffModel
import com.google.gson.annotations.SerializedName
data class StaffModel(
    @SerializedName("data") val data: List<StaffData>
)