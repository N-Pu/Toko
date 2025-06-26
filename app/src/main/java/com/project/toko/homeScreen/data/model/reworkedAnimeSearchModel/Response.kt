package com.project.toko.homeScreen.data.model.reworkedAnimeSearchModel

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Response(

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null
) : Parcelable

@Parcelize
data class Pagination(

	@field:SerializedName("has_next_page")
	val hasNextPage: Boolean? = null,

	@field:SerializedName("last_visible_page")
	val lastVisiblePage: Int? = null,

	@field:SerializedName("items")
	val items: Items? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
) : Parcelable

@Parcelize
data class Jpg(

	@field:SerializedName("large_image_url")
	val largeImageUrl: String? = null,

	@field:SerializedName("small_image_url")
	val smallImageUrl: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null
) : Parcelable

@Parcelize
data class ProducersItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class ThemesItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class DemographicsItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("title_japanese")
	val titleJapanese: String? = null,

	@field:SerializedName("favorites")
	val favorites: Int? = null,

	@field:SerializedName("broadcast")
	val broadcast: Broadcast? = null,

	@field:SerializedName("year")
	val year: Int? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("scored_by")
	val scoredBy: Int? = null,

	@field:SerializedName("title_synonyms")
	val titleSynonyms: List<String?>? = null,

	@field:SerializedName("source")
	val source: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("trailer")
	val trailer: Trailer? = null,

	@field:SerializedName("duration")
	val duration: String? = null,

	@field:SerializedName("score")
	val score: Int? = null,

	@field:SerializedName("themes")
	val themes: List<ThemesItem?>? = null,

	@field:SerializedName("approved")
	val approved: Boolean? = null,

	@field:SerializedName("genres")
	val genres: List<GenresItem?>? = null,

	@field:SerializedName("popularity")
	val popularity: Int? = null,

	@field:SerializedName("members")
	val members: Int? = null,

	@field:SerializedName("title_english")
	val titleEnglish: String? = null,

	@field:SerializedName("rank")
	val rank: Int? = null,

	@field:SerializedName("season")
	val season: String? = null,

	@field:SerializedName("airing")
	val airing: Boolean? = null,

	@field:SerializedName("episodes")
	val episodes: Int? = null,

	@field:SerializedName("aired")
	val aired: Aired? = null,

	@field:SerializedName("images")
	val images: Images? = null,

	@field:SerializedName("studios")
	val studios: List<StudiosItem?>? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("titles")
	val titles: List<TitlesItem?>? = null,

	@field:SerializedName("synopsis")
	val synopsis: String? = null,

	@field:SerializedName("explicit_genres")
	val explicitGenres: List<ExplicitGenresItem?>? = null,

	@field:SerializedName("licensors")
	val licensors: List<LicensorsItem?>? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("producers")
	val producers: List<ProducersItem?>? = null,

	@field:SerializedName("background")
	val background: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("demographics")
	val demographics: List<DemographicsItem?>? = null
) : Parcelable

@Parcelize
data class Broadcast(

	@field:SerializedName("string")
	val string: String? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("day")
	val day: String? = null
) : Parcelable

@Parcelize
data class To(

	@field:SerializedName("month")
	val month: Int? = null,

	@field:SerializedName("year")
	val year: Int? = null,

	@field:SerializedName("day")
	val day: Int? = null
) : Parcelable

@Parcelize
data class Images(

	@field:SerializedName("jpg")
	val jpg: Jpg? = null,

	@field:SerializedName("webp")
	val webp: Webp? = null
) : Parcelable

@Parcelize
data class StudiosItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class TitlesItem(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("title")
	val title: String? = null
) : Parcelable

@Parcelize
data class LicensorsItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class ExplicitGenresItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class Items(

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("count")
	val count: Int? = null
) : Parcelable

@Parcelize
data class From(

	@field:SerializedName("month")
	val month: Int? = null,

	@field:SerializedName("year")
	val year: Int? = null,

	@field:SerializedName("day")
	val day: Int? = null
) : Parcelable

@Parcelize
data class Aired(

	@field:SerializedName("prop")
	val prop: Prop? = null,

	@field:SerializedName("from")
	val from: String? = null,

	@field:SerializedName("to")
	val to: String? = null
) : Parcelable

@Parcelize
data class Prop(

	@field:SerializedName("string")
	val string: String? = null,

	@field:SerializedName("from")
	val from: From? = null,

	@field:SerializedName("to")
	val to: To? = null
) : Parcelable

@Parcelize
data class Trailer(

	@field:SerializedName("embed_url")
	val embedUrl: String? = null,

	@field:SerializedName("youtube_id")
	val youtubeId: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class Webp(

	@field:SerializedName("large_image_url")
	val largeImageUrl: String? = null,

	@field:SerializedName("small_image_url")
	val smallImageUrl: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null
) : Parcelable

@Parcelize
data class GenresItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mal_id")
	val malId: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable
