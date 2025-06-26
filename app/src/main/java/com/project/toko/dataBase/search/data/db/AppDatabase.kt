package com.project.toko.dataBase.search.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.toko.dataBase.search.data.db.converters.Converters
import com.project.toko.dataBase.search.data.db.dao.AnimeDao
import com.project.toko.dataBase.search.data.db.dao.AnimeOrderDao
import com.project.toko.dataBase.search.data.db.dao.PaginationDao
import com.project.toko.dataBase.search.data.db.dao.SearchQueriesDao
import com.project.toko.dataBase.search.data.db.entity.PaginationEntity
import com.project.toko.dataBase.search.data.db.entity.AnimeEntity
import com.project.toko.dataBase.search.data.db.entity.AnimeOrderEntity
import com.project.toko.dataBase.search.data.db.entity.SearchQueryEntity

// AppDatabase.kt
@Database(
    entities = [AnimeEntity::class, SearchQueryEntity::class, PaginationEntity::class, AnimeOrderEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
    abstract fun paginationDao(): PaginationDao
    abstract fun searchQueriesDao(): SearchQueriesDao
    abstract fun animeOrderDao(): AnimeOrderDao
}
