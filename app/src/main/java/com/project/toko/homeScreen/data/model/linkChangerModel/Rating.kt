package com.project.toko.homeScreen.data.model.linkChangerModel



data class Rating(val name: String
//                  , var isSelected: Boolean = false
)

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