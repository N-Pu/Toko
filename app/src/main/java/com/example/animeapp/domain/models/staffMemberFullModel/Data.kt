package com.example.animeapp.domain.models.staffMemberFullModel
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("about") val about: String,
    @SerializedName("alternate_names") val alternate_names: List<String>,
    @SerializedName("anime") val anime: List<com.example.animeapp.domain.models.staffMemberFullModel.Anime>,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("family_name") val family_name: String,
    @SerializedName("favorites") val favorites: Int,
    @SerializedName("given_name") val given_name: String,
    @SerializedName("images") val images: com.example.animeapp.domain.models.staffMemberFullModel.ImagesX,
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("manga") val manga: List<com.example.animeapp.domain.models.staffMemberFullModel.Manga>,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    @SerializedName("voices") val voices: List<com.example.animeapp.domain.models.staffMemberFullModel.Voice>,
    @SerializedName("website_url") val website_url: String
)