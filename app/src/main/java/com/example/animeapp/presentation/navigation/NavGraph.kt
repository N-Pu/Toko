package com.example.animeapp.presentation.navigation


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.animeapp.presentation.firstScreen.MainScreen
import com.example.animeapp.presentation.secondScreen.DetailScreen

private var isLogPrinted = false

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) {
            val id = it.arguments?.getInt("id")
            if (id != null) {
                DetailScreen(navController = navController, id = id)
                if (!isLogPrinted) {
                    Log.d("Arg", "ID (IN SetupNavGraph) $id")
                    isLogPrinted = true
                }
            }
        }
    }
}


