package com.project.toko.domain.models.staffModel
import com.google.gson.annotations.SerializedName
data class StaffModel(
    @SerializedName("data") val data: List<Data>
)