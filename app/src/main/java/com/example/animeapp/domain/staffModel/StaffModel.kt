package com.example.animeapp.domain.staffModel
import com.google.gson.annotations.SerializedName
data class StaffModel(
    @SerializedName("data") val data: List<Data>
)