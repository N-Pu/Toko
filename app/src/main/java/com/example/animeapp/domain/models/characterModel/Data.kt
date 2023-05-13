
package com.example.animeapp.domain.models.characterModel

import com.google.gson.annotations.SerializedName


data class Data(
    @SerializedName("about") val about: String,
    @SerializedName("anime") val anime: List<Anime>,
    @SerializedName("favorites") val favorites: Int,
    @SerializedName("images") val images: ImagesX,
    @SerializedName("mal_id") val mal_id: Int,
    @SerializedName("manga") val manga: List<Manga>,
    @SerializedName("name") val name: String,
    @SerializedName("name_kanji") val name_kanji: String,
    @SerializedName("nicknames") val nicknames: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("voices") val voices: List<Voice>
)