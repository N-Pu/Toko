package com.project.toko.homeScreen.data.model.linkChangerModel

import com.project.toko.homeScreen.ui.homeScreen.FilterItem


data class Rating(
    override val name: String
//                  , var isSelected: Boolean = false
): FilterItem

fun getRating(): List<Rating> {
    return listOf(
        Rating("g"),
        Rating("pg"),
        Rating("pg13"),
        Rating("r17"),
        Rating("r"),
        Rating("rx")
    )
}