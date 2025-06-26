package com.project.toko.dataBase.search.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

// AnimeEntity.kt
@Entity(
    tableName = "anime",
    indices = [Index("mal_id")]
)
data class AnimeEntity(
    @PrimaryKey
    val mal_id: Int,
    val uuid: String = UUID.randomUUID().toString(),
    val url: String?,
    val images: AnimeImages?,
    val trailer: AnimeTrailer?,
    val approved: Boolean?,
    val titles: List<AnimeTitle>,
    val title: String?,
    val title_english: String?,
    val title_japanese: String?,
    val title_synonyms: List<String?>?,
    val type: String?,
    val source: String?,
    val episodes: Int?,
    val status: String?,
    val airing: Boolean?,
    val aired: Aired?,
    val duration: String?,
    val rating: String?,
    val score: Float?,
    val scored_by: Float?,
    val rank: Int?,
    val popularity: Int?,
    val members: Int?,
    val favorites: Int?,
    val synopsis: String?,
    val background: String?,
    val season: String?,
    val year: Int?,
    val broadcast: Broadcast?,
    val producers: List<Producer?>?,
    val licensors: List<Licensor?>?,
    val studios: List<Studio?>?,
    val genres: List<Genre?>?,
    val explicit_genres: List<ExplicitGenre?>?,
    val themes: List<Theme?>?,
    val demographics: List<Demographic?>,
)

// Other nested data classes
data class AnimeImages(
    val jpg: ImageType?,
    val webp: ImageType?
)

data class ImageType(
    val image_url: String?,
    val small_image_url: String?,
    val large_image_url: String?
)

data class AnimeTrailer(
    val youtube_id: String?,
    val url: String?,
    val embed_url: String?
)

data class AnimeTitle(
    val type: String?,
    val title: String?
)

data class Aired(
    val from: String?,
    val to: String?,
    val prop: AiredProp?
)

data class AiredProp(
    val from: AiredDateFrom?,
    val to: AiredDateTo?,
    val string: String?
)

data class AiredDateTo(
    val day: Int?,
    val month: Int?,
    val year: Int?
)

data class AiredDateFrom(
    val day: Int?,
    val month: Int?,
    val year: Int?
)

data class Broadcast(
    val day: String?,
    val time: String?,
    val timezone: String?,
    val string: String?
)

data class Producer(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class Licensor(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class Studio(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class Genre(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class Demographic(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class ExplicitGenre(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class Theme(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)
