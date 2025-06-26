package com.project.toko.dataBase.search.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_queries")
data class SearchQueryEntity(
    @PrimaryKey(autoGenerate = true)
    val majorId: Long = 0,
    val filtersJson: String , // JSON строки всех фильтров
    val createdAt: Long = System.currentTimeMillis(),
)
