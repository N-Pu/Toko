package com.project.toko.detailScreen.model.staffModel

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class StaffData(
    @SerializedName("person") val person: Person,
    @SerializedName("positions") val positions: List<String>
)