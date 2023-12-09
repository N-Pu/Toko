package com.project.toko.core.presentation_layer.navigation

import com.project.toko.R


sealed class Screen(val route: String, val iconId: Int?, val contentDescription: String) {

//    object Splash : Screen("splash_screen", null, "Splash Screen")
    object Home : Screen("home_screen", R.drawable.home, "Search Anime")
    object Detail : Screen("detail_screen/{id}", R.drawable.detail, "Details on Anime")
    object Favorites : Screen("favorites_screen", R.drawable.bookmarkempty, "Favorite anime")
    object RandomAnimeOrManga :
        Screen("random_anime_or_manga_screen", R.drawable.shuffle, "Current News on Anime")

    object Nothing : Screen("no_id", null, "No id screen")

    object DetailOnWholeCast :
        Screen("detail_on_whole_cast/{id}", null, "Details on whole cast of current anime")

    object DetailOnWholeStaff :
        Screen("detail_on_whole_staff", null, "Details on whole staff of current anime")

    object CharacterDetail :
        Screen("detail_on_character/{id}", null, "Details on current character")


    object StaffDetail : Screen("detail_on_staff/{id}", null, "Details on current staff member")

    object ProducerDetail :
        Screen("detail_on_producer/{id}/{studio_name}", null, "Details on current producer member")
}