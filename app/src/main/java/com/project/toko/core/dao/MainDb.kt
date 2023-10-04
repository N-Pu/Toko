package com.project.toko.core.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AnimeItem::class], version = 2, exportSchema = false)
abstract class MainDb : RoomDatabase() {
    abstract fun getDao(): Dao
}
