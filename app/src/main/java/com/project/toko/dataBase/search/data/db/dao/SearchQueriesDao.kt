package com.project.toko.dataBase.search.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.project.toko.dataBase.search.data.db.entity.SearchQueryEntity



@Dao
interface SearchQueriesDao {

    // Поиск ID по filtersJson (работает, т.к. это @Query)
    @Query("SELECT majorId FROM search_queries WHERE filtersJson = :filtersJson LIMIT 1")
    suspend fun findQueryIdByFilters(filtersJson: String): Long?

    // Вставка (принимает Entity, а не строку!)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQueryFilters(searchQueryEntity: SearchQueryEntity): Long  // Возвращает ID новой записи

    // Удаление (принимает Entity, а не строку!)
    @Delete
    suspend fun deleteQueryFilters(searchQueryEntity: SearchQueryEntity)

    // Получение даты (работает, т.к. это @Query)
    @Query("SELECT createdAt FROM search_queries WHERE majorId = :id LIMIT 1")
    suspend fun getDateWhenQueryWasUsedFirstTime(id: Long): Long


}