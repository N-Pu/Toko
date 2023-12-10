package com.project.toko.homeScreen.model.tabRow

data class TabItem(
    val title: String,
)

fun returnListOfTabItems(): List<TabItem> {
    return listOf(
        TabItem("Type"),
        TabItem("Genres"),
        TabItem("Rating"),
        TabItem("Score"),
        TabItem("Order By")
    )
}

