package com.project.toko.characterDetailedScreen.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characterItem")
data class CharacterItem(
    @PrimaryKey
    val id: Int? = null,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "anime")
    val anime: String,
    @ColumnInfo(name = "image")
    val image: String,
)
