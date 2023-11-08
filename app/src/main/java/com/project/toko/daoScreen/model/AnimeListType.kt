package com.project.toko.daoScreen.model

enum class AnimeListType(val route: String) {
    WATCHING("Watching"),
    PLANNED("Plan to Watch"),
    COMPLETED("Completed"),
    DROPPED("Dropped"),
    FAVORITE("Favorite"),
    PERSON("Person"),
    CHARACTER("Character")
}
