package com.project.toko.personDetailedScreen.dao


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personItem")
data class PersonItem(
    @PrimaryKey
    val id: Int? = null,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "givenName")
    val givenName: String?,
    @ColumnInfo(name = "familyName")
    val familyName: String?,
    @ColumnInfo(name = "image")
    val image: String,
)
