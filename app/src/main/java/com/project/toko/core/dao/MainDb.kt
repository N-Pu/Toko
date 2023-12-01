package com.project.toko.core.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.toko.characterDetailedScreen.dao.CharacterItem
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.daoScreen.dao.FavoriteItem
import com.project.toko.personDetailedScreen.dao.PersonItem

@Database(
    entities = [AnimeItem::class, FavoriteItem::class, CharacterItem::class, PersonItem::class],
    version = 11,
    exportSchema = false
)
abstract class MainDb : RoomDatabase() {
    abstract fun getDao(): Dao
}
