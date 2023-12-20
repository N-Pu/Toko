package com.project.toko.detailScreen.model.staffModel
import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName
@Immutable
data class StaffModel(
    @SerializedName("data") val data: List<StaffData>
)