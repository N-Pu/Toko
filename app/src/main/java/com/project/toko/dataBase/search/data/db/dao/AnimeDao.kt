package com.project.toko.dataBase.search.data.db.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.project.toko.dataBase.search.data.db.entity.AnimeEntity
import com.project.toko.dataBase.search.data.db.entity.PaginationEntity
import com.project.toko.dataBase.search.data.db.entity.SearchQueryEntity
import kotlinx.coroutines.flow.Flow


// AnimeDao.kt
@Dao
interface AnimeDao {

    @Query("DELETE FROM anime WHERE 'majorId' = :majorId")
    suspend fun clearByQuery(majorId: Long)

//    @Query("SELECT * FROM anime WHERE 'majorId' = :majorId ORDER BY 'order' ASC")
//    fun getListOfAnimeByMajorId(majorId: Long): Flow<List<AnimeEntity>>

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insert(listOfAnime: List<AnimeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Изменяем стратегию конфликта
    suspend fun insert(listOfAnime: List<AnimeEntity>)

//    @Query("DELETE FROM anime WHERE 'majorId' = :majorId")
//    suspend fun deleteByMajorId(majorId: Long)

    @Query("DELETE FROM anime")
    suspend fun clearAllAnime()

    @Query("SELECT * FROM anime WHERE mal_id = :mal_id LIMIT 1")
    suspend fun getAnimeByMalId(mal_id: Int): AnimeEntity

    @Query(
        """
    SELECT a.* FROM anime a
    INNER JOIN anime_order o ON a.mal_id = o.malId
    WHERE o.majorId = :majorId
    ORDER BY o.`order` ASC
"""
    )
    fun getListOfAnimeByMajorId(majorId: Long): Flow<List<AnimeEntity>>



//    // excludes data from animeDao
//    @Transaction
//    suspend fun deleteAllDataByFilterJson(filtersJson: String){
//        val majorId = findQueryIdByFilters(filtersJson)
//        deleteDataFromByMajorIdSearchQueries(majorId)
//        clearPaginationDataByMajorId(majorId)
//        deleteAnimeOrderByMajorId(majorId)
//    }
//
//    @Query("SELECT majorId FROM search_queries WHERE filtersJson = :filtersJson LIMIT 1")
//    suspend fun findQueryIdByFilters(filtersJson: String): Long
//
//    @Query("DELETE FROM search_queries WHERE majorId = :majorId ")
//    suspend fun deleteDataFromByMajorIdSearchQueries(majorId: Long)
//
//
//    @Query("DELETE FROM pagination_data WHERE majorId = :id")
//    suspend fun clearPaginationDataByMajorId(id: Long)
//
//    @Query("DELETE FROM anime_order WHERE majorId = :majorId")
//    suspend fun deleteAnimeOrderByMajorId(majorId: Long)



    // excludes data from animeDao
//    @Transaction
//    suspend fun deleteAllDataByFilterJson(filtersJson: String){
//        val majorId = findQueryIdByFilters(filtersJson)
//            if (majorId != null) {
//                deleteDataFromByMajorIdSearchQueries(majorId)
//                clearPaginationDataByMajorId(majorId)
//                deleteAnimeOrderByMajorId(majorId)
//            }
////            else{
////
////            }
//    }

    @Query("SELECT majorId FROM search_queries WHERE filtersJson = :filtersJson LIMIT 1")
    suspend fun findQueryIdByFilters(filtersJson: String): Long?
    @Query("DELETE FROM search_queries WHERE majorId = :majorId ")
    suspend fun deleteDataFromByMajorIdSearchQueries(majorId: Long)
    @Query("DELETE FROM pagination_data WHERE majorId = :id")
    suspend fun clearPaginationDataByMajorId(id: Long)
//    @Query("DELETE FROM anime_order WHERE majorId = :majorId")
//    suspend fun deleteAnimeOrderByMajorId(majorId: Long)
    @Query("""DELETE FROM anime WHERE mal_id NOT IN (SELECT malId FROM anime_order)""")
    suspend fun deleteOrphanedAnime()

    @Query("DELETE FROM anime_order WHERE majorId = :majorId")
    suspend fun deleteAnimeOrderByMajorId(majorId: Long) {
        Log.d("Database", "Deleting anime_order for majorId: $majorId")
        // Реальная реализация будет сгенерирована Room
    }


    @Transaction
    suspend fun deleteAllDataByFilterJson(majorId: Long) {
        try {
            // 1. Сначала удаляем данные из зависимых таблиц
            deleteAnimeOrderByMajorId(majorId)  // Зависит от pagination_data и search_queries
            clearPaginationDataByMajorId(majorId)  // Зависит от search_queries

            // 2. Затем удаляем основную запись
            deleteDataFromByMajorIdSearchQueries(majorId)

            // 3. Чистим "осиротевшие" записи
            deleteOrphanedAnime()

            Log.d("Database", "Successfully deleted all data for majorId: $majorId")
        } catch (e: Exception) {
            Log.e("Database", "Error deleting data for majorId: $majorId", e)
            throw e  // Пробрасываем исключение дальше
        }
    }


//
}




