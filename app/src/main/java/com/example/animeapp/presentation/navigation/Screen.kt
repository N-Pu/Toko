package com.example.animeapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String, val icon: ImageVector, val contentDescription: String) {
    object Home : Screen("home_screen", Icons.Filled.Search, "Search Anime")
    object Detail : Screen("detail_screen/{id}", Icons.Filled.List, "Details on Anime")
    object RandomAnimeOrManga :
        Screen("random_anime_or_manga_screen", Icons.Filled.ShoppingCart, "Current News on Anime")

    object Favorites : Screen("favorites_screen", Icons.Filled.FavoriteBorder, "Favorite anime")

}

object Nothing {
    const val value = "no_id"
}

object DetailOnCast {
    const val value = "detail_on_cast"
}

object DetailOnStaff {
    const val value = "detail_on_staff"
}

object CharacterDetail {
    const val value = "detail_on_character/{id}"
}

object StaffDetail {
    const val value = "detail_on_staff/{id}"
}

object ProducerDetail {
    const val value = "detail_on_producer/{id}/{studio_name}"
}