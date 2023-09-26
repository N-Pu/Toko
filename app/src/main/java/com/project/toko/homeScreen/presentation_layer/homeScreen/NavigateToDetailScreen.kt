package com.project.toko.homeScreen.presentation_layer.homeScreen

import androidx.navigation.NavController


fun navigateToDetailScreen(navController: NavController, mal_id: Int) {
    navController.navigate(route = "detail_screen/$mal_id") {
        launchSingleTop = true
    }
}
