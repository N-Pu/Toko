package com.project.toko.dataBase.search.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "pagination_data",
    foreignKeys = [
        ForeignKey(
            entity = SearchQueryEntity::class,
            parentColumns = ["majorId"],
            childColumns = ["majorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PaginationEntity(
    @PrimaryKey(autoGenerate = false) val majorId: Long,
    @Embedded val pagination: Pagination
)

data class Pagination(
    val lastVisiblePage: Int,
    val hasNextPage: Boolean,
    val currentPage: Int,
    @Embedded val items: PaginationItems
)

data class PaginationItems(
    val count: Int,
    val total: Int,
    val perPage: Int
)