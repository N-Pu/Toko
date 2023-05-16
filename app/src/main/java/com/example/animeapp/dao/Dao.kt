package com.example.animeapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

    // Функция для добавления аниме в категорию

    //    @Query("UPDATE animeItems SET category = :category, anime = :anime, score = :score, scored_by = :scoredBy, animeImage = :animeImage WHERE id = :animeId")
//    fun addToCategory(
//        animeId: Int,
//        category: String,
//        anime: String,
//        score: String,
//        scoredBy: String,
//        animeImage: String
//    )
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToCategory(animeItem: AnimeItem)


    // Функция для удаления аниме из категории
    @Query("UPDATE animeItems SET category = NULL WHERE id = :animeId")
    fun removeFromCategory(animeId: Int)

    // Функция для удаления всех аниме из всех категорий
    @Query("UPDATE animeItems SET category = NULL")
    fun removeAllCategories()

    // Функция для получения всех аниме в определенной категории
    @Query("SELECT * FROM animeItems WHERE category = :category")
    fun getAnimeInCategory(category: String): Flow<List<AnimeItem>>

    // Функция для получения всех аниме в категории WATCHING
    @Query("SELECT * FROM animeItems WHERE category = 'Watching'")
    fun getWatchingAnime(): Flow<List<AnimeItem>>

    // Функция для получения всех аниме в категории DROPPED
    @Query("SELECT * FROM animeItems WHERE category = 'Dropped'")
    fun getDroppedAnime(): Flow<List<AnimeItem>>

    // Функция для получения всех аниме в категории WATCHED
    @Query("SELECT * FROM animeItems WHERE category = 'Watched'")
    fun getWatchedAnime(): Flow<List<AnimeItem>>

    // Функция для получения всех аниме в категории PLANNED
    @Query("SELECT * FROM animeItems WHERE category = 'Planned'")
    fun getPlannedAnime(): Flow<List<AnimeItem>>
}
