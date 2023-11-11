package com.project.toko.daoScreen.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animeItems")
data class AnimeItem(
    @PrimaryKey
    val id: Int? = null,

    @ColumnInfo(name = "animeName")
    val animeName: String,
    @ColumnInfo(name = "score")
    val score: String,
    @ColumnInfo(name = "scored_by")
    val scored_by: String,
    @ColumnInfo(name = "animeImage")
    val animeImage: String,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "rating")
    val rating: String,
    @ColumnInfo(name = "secondName")
    val secondName: String,
    @ColumnInfo(name = "airedFrom")
    val airedFrom: String,
    @ColumnInfo(name = "category")
    val category: String? = null,
    @ColumnInfo(name = "type")
    val type: String
)
