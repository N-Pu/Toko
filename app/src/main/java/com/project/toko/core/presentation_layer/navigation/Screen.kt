package com.project.toko.core.presentation_layer.navigation

import androidx.compose.runtime.Immutable

@Immutable
sealed class Screen(val route: String, val contentDescription: String) {

    object Home : Screen("home_screen", "Search Anime")
    object Detail : Screen("detail_screen/{id}", "Details on Anime")
    object Favorites : Screen("favorites_screen", "Favorite anime")
    object RandomAnimeOrManga :
        Screen("random_anime_or_manga_screen", "Current News on Anime")

    object Nothing : Screen("no_id", "No id screen")

    object DetailOnWholeCast :
        Screen("detail_on_whole_cast/{id}", "Details on whole cast of current anime")

    object DetailOnWholeStaff :
        Screen("detail_on_whole_staff", "Details on whole staff of current anime")

    object CharacterDetail :
        Screen("detail_on_character/{id}", "Details on current character")

    object StaffDetail : Screen("detail_on_staff/{id}", "Details on current staff member")

}