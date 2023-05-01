package com.example.animeapp.presentation.firstScreen

import androidx.navigation.NavController
import com.example.animeapp.presentation.navigation.Screen

fun navigateToDetailScreen(navController: NavController, mal_id: Int) {
    navController.navigate(route = "detail_screen/$mal_id"){
        launchSingleTop = true
        popUpTo(Screen.Home.route){
            saveState = true
        }
        restoreState = true
    }
}
