package com.project.toko.dataBase.search.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.project.toko.dataBase.search.data.db.entity.PaginationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaginationDao {
//    @Insert
//    suspend fun insert(pagination: PaginationEntity)
//
//    @Update
//    suspend fun update(pagination: PaginationEntity)

    @Upsert
    suspend fun updateOrInsert(pagination: PaginationEntity)

    @Query("SELECT * FROM pagination_data WHERE majorId = :id LIMIT 1")
    suspend fun getPagination(id: Long): PaginationEntity?

    @Query("SELECT hasNextPage FROM pagination_data WHERE majorId = :id")
    suspend fun hasNextPage(id: Long): Boolean

    @Query("DELETE FROM pagination_data")
    suspend fun clearAll()

//    @Query("DELETE FROM pagination_data WHERE majorId = :id")
//    suspend fun clearByMajorId(id: Long)
}