package com.example.animeapp.presentation.screens.homeScreen

import androidx.navigation.NavController


fun navigateToDetailScreen(navController: NavController, mal_id: Int) {
    navController.navigate(route = "detail_screen/$mal_id") {
        launchSingleTop = true
    }
}
