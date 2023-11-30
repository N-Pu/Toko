package com.project.toko.daoScreen.model
enum class AnimeListType(
    val route: String,
) {
    WATCHING("Watching"),
    PLANNED("Planned"),
    COMPLETED("Watched"),
    DROPPED("Dropped"),
    FAVORITE("Favorite"),
    PERSON("Person"),
    CHARACTER("Character")
}
