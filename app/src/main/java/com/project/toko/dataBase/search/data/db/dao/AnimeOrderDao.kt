package com.project.toko.dataBase.search.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.toko.dataBase.search.data.db.entity.AnimeOrderEntity

@Dao
interface AnimeOrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<AnimeOrderEntity>)

//    @Query("DELETE FROM anime_order WHERE majorId = :majorId")
//    suspend fun deleteByMajorId(majorId: Long)

    @Query("SELECT MAX(`order`) FROM anime_order WHERE majorId = :majorId")
    suspend fun getLastOrder(majorId: Long): Int?


    @Query("SELECT COUNT(*) FROM anime_order WHERE majorId = :majorId")
    suspend fun getAnimeCountByMajorId(majorId: Long): Int
}