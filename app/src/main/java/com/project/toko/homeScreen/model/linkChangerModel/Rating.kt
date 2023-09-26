package com.project.toko.homeScreen.model.linkChangerModel



data class Rating(val ratingName: String, var isSelected: Boolean = false)

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