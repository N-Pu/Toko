
package com.project.toko.producerDetailedScreen.model.producerModel
import com.google.gson.annotations.SerializedName
data class Data(
    @SerializedName("about") val about: String,
    @SerializedName("count") val count: Int,
    @SerializedName("established") val established: String,
    @SerializedName("external") val external: List<External>,
    @SerializedName("favorites") val favorites: Int,
    @SerializedName("images") val images: Images,
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("titles") val titles: List<Title>,
    @SerializedName("url") val url: String
)