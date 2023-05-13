package com.example.animeapp.domain.models.staffModel
import com.google.gson.annotations.SerializedName
data class StaffModel(
    @SerializedName("data") val data: List<Data>
)