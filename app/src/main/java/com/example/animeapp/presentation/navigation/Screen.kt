package com.example.animeapp.presentation.navigation

import com.example.animeapp.R


sealed class Screen(val route: String, val iconId: Int, val contentDescription: String) {
    object Home : Screen("home_screen", R.drawable.home, "Search Anime")
    object Detail : Screen("detail_screen/{id}", R.drawable.media, "Details on Anime")
    object Favorites : Screen("favorites_screen", R.drawable.favorite, "Favorite anime")
    object RandomAnimeOrManga :
        Screen("random_anime_or_manga_screen", R.drawable.random, "Current News on Anime")



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