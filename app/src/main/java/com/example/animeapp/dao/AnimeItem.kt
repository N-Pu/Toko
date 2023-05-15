package com.example.animeapp.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animeItems")
data class AnimeItem(
    @PrimaryKey
    var id: Int? = null,

    @ColumnInfo(name = "anime")
    var anime: String,
    @ColumnInfo(name = "score")
    var score: String,
    @ColumnInfo(name = "scored_by")
    var scored_by: String,
    @ColumnInfo(name = "animeImage")
    var animeImage: String,
    @ColumnInfo(name = "category")
    var category: String? = null
)
