package com.project.toko.homeScreen.data.model.newAnimeSearchModel
import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class NewAnimeSearchModel(
    @SerializedName("data") val data: List<AnimeSearchData> = emptyList(),
    @SerializedName("pagination") val pagination: Pagination
)