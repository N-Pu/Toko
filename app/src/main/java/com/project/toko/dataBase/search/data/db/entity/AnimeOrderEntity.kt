package com.project.toko.dataBase.search.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "anime_order",
    primaryKeys = ["majorId", "malId"],
    foreignKeys = [
        ForeignKey(
            entity = SearchQueryEntity::class,
            parentColumns = ["majorId"],
            childColumns = ["majorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AnimeOrderEntity(
    val majorId: Long,
    val malId: Int,
    val order: Int
)