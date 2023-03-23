package com.example.animeapp.ui.domain.exampleModels

data class Pagination(
    val has_next_page: Boolean,
    val items: com.example.animeapp.ui.domain.exampleModels.Items,
    val last_visible_page: Int
)