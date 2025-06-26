package com.project.toko.dataBase.search.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.toko.dataBase.search.data.db.entity.Aired
import com.project.toko.dataBase.search.data.db.entity.AnimeImages
import com.project.toko.dataBase.search.data.db.entity.AnimeTitle
import com.project.toko.dataBase.search.data.db.entity.AnimeTrailer
import com.project.toko.dataBase.search.data.db.entity.Broadcast
import com.project.toko.dataBase.search.data.db.entity.Demographic
import com.project.toko.dataBase.search.data.db.entity.ExplicitGenre
import com.project.toko.dataBase.search.data.db.entity.Genre
import com.project.toko.dataBase.search.data.db.entity.Licensor
import com.project.toko.dataBase.search.data.db.entity.Producer
import com.project.toko.dataBase.search.data.db.entity.Studio
import com.project.toko.dataBase.search.data.db.entity.Theme

// Converters.kt
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromImages(images: AnimeImages): String =
        gson.toJson(images)

    @TypeConverter
    fun toImages(imagesString: String): AnimeImages =
        gson.fromJson(imagesString, AnimeImages::class.java)



    // AnimeTrailer
    @TypeConverter
    fun fromTrailer(trailer: AnimeTrailer?): String? =
        trailer?.let { gson.toJson(it) }

    @TypeConverter
    fun toTrailer(trailerString: String?): AnimeTrailer? =
        trailerString?.let { gson.fromJson(it, AnimeTrailer::class.java) }

    // List<AnimeTitle>
    @TypeConverter
    fun fromTitleList(titles: List<AnimeTitle>?): String? =
        titles?.let { gson.toJson(it) }

    @TypeConverter
    fun toTitleList(titlesString: String?): List<AnimeTitle>? =
        titlesString?.let { gson.fromJson(it, object : TypeToken<List<AnimeTitle>>() {}.type) }

    // List<String>
    @TypeConverter
    fun fromStringList(list: List<String>?): String? =
        list?.let { gson.toJson(it) }

    @TypeConverter
    fun toStringList(listString: String?): List<String>? =
        listString?.let { gson.fromJson(it, object : TypeToken<List<String>>() {}.type) }

    // Aired
    @TypeConverter
    fun fromAired(aired: Aired?): String? =
        aired?.let { gson.toJson(it) }

    @TypeConverter
    fun toAired(airedString: String?): Aired? =
        airedString?.let { gson.fromJson(it, Aired::class.java) }

    // Broadcast
    @TypeConverter
    fun fromBroadcast(broadcast: Broadcast?): String? =
        broadcast?.let { gson.toJson(it) }

    @TypeConverter
    fun toBroadcast(broadcastString: String?): Broadcast? =
        broadcastString?.let { gson.fromJson(it, Broadcast::class.java) }

    // List<Producer>
    @TypeConverter
    fun fromProducerList(producers: List<Producer>?): String? =
        producers?.let { gson.toJson(it) }

    @TypeConverter
    fun toProducerList(producersString: String?): List<Producer>? =
        producersString?.let { gson.fromJson(it, object : TypeToken<List<Producer>>() {}.type) }
    // Similar converters for other complex types:
    // AnimeTrailer, List<AnimeTitle>, Aired, Broadcast,
    // List<Producer>, List<Licensor>, List<Studio>, List<Genre>


    @TypeConverter
    fun fromLicensorList(licensors: List<Licensor>?): String? =
        licensors?.let { gson.toJson(it) }

    @TypeConverter
    fun toLicensorList(licensorsString: String?): List<Licensor>? =
        licensorsString?.let { gson.fromJson(it, object : TypeToken<List<Licensor>>() {}.type) }

    // Для List<Studio>
    @TypeConverter
    fun fromStudioList(studios: List<Studio>?): String? =
        studios?.let { gson.toJson(it) }

    @TypeConverter
    fun toStudioList(studiosString: String?): List<Studio>? =
        studiosString?.let { gson.fromJson(it, object : TypeToken<List<Studio>>() {}.type) }

    // Для List<Genre> (используется для genres, explicit_genres, themes, demographics)
    @TypeConverter
    fun fromGenreList(genres: List<Genre>?): String? =
        genres?.let { gson.toJson(it) }

    @TypeConverter
    fun toGenreList(genresString: String?): List<Genre>? =
        genresString?.let { gson.fromJson(it, object : TypeToken<List<Genre>>() {}.type) }


    @TypeConverter
    fun fromExplicitGenreList(value: List<ExplicitGenre>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toExplicitGenreList(value: String): List<ExplicitGenre> {
        val listType = object : TypeToken<List<ExplicitGenre>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromThemeList(value: List<Theme>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toThemeList(value: String): List<Theme> {
        val listType = object : TypeToken<List<Theme>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromDemographicList(value: List<Demographic>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDemographicList(value: String): List<Demographic> {
        val listType = object : TypeToken<List<Demographic>>() {}.type
        return gson.fromJson(value, listType)
    }
}