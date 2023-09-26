package com.project.toko.homeScreen.model.staffModel
import com.google.gson.annotations.SerializedName
data class StaffModel(
    @SerializedName("data") val data: List<Data>
)