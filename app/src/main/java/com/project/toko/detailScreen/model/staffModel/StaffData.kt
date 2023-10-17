package com.project.toko.detailScreen.model.staffModel

import com.google.gson.annotations.SerializedName

data class StaffData(
    @SerializedName("person") val person: Person,
    @SerializedName("positions") val positions: List<String>
)