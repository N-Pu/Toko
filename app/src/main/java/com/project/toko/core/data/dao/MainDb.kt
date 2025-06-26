package com.project.toko.core.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.toko.characterDetailedScreen.data.model.dao.CharacterItem
import com.project.toko.savedScreen.data.dao.AnimeItem
import com.project.toko.savedScreen.data.dao.FavoriteItem
import com.project.toko.personDetailedScreen.data.dao.PersonItem

@Database(
    entities = [AnimeItem::class, FavoriteItem::class, CharacterItem::class, PersonItem::class],
    version = 11,
    exportSchema = false
)
abstract class MainDb : RoomDatabase() {
    abstract fun getDao(): Dao
}
