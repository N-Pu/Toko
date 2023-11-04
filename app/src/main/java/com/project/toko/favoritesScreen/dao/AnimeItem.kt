package com.project.toko.favoritesScreen.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animeItems")
data class AnimeItem(
    @PrimaryKey
    val id: Int? = null,

    @ColumnInfo(name = "anime")
    val anime: String,
    @ColumnInfo(name = "score")
    val score: String,
    @ColumnInfo(name = "scored_by")
    val scored_by: String,
    @ColumnInfo(name = "animeImage")
    val animeImage: String,
    @ColumnInfo(name = "category")
    val category: String? = null
)
