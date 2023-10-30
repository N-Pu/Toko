package com.project.toko.core.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

    // Функция для добавления аниме в категорию
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCategory(animeItem: AnimeItem)

    // Функция для получения всех аниме в определенной категории
    @Query("SELECT * FROM animeItems WHERE category = :category")
    fun getAnimeInCategory(category: String): Flow<List<AnimeItem>>

    @Query("DELETE FROM animeItems WHERE id = :id")
    suspend fun removeFromDataBase(id: Int)

    // Функция для проверки наличия ID в базе данных
    @Query("SELECT EXISTS(SELECT 1 FROM animeItems WHERE id = :id LIMIT 1)")
    fun containsInDataBase(id: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM animeItems WHERE id = :id AND category = :categoryId LIMIT 1)")
    fun containsItemIdInCategory(id: Int, categoryId: String): Flow<Boolean>

    @Query("SELECT category FROM animeItems WHERE id = :id")
    fun getCategoryForId(id: Int): Flow<String?>

}
