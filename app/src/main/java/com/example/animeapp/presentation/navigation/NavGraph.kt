package com.example.animeapp.presentation.navigation


import HomeScreenViewModel
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.animeapp.presentation.favoritesScreen.Fav
import com.example.animeapp.presentation.firstScreen.MainScreen
import com.example.animeapp.presentation.newsScreen.News
import com.example.animeapp.presentation.noId.NoId
import com.example.animeapp.presentation.secondScreen.DetailScreen
import com.example.animeapp.viewModel.IdViewModel



@Composable
fun SetupNavGraph(
    navController: NavHostController,
 viewModelProvider: ViewModelProvider
//    savedAnimeViewModel: SavedAnimeViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(route = Screen.Home.route) {
            MainScreen(
                navController = navController,
                searchViewModel = viewModelProvider[HomeScreenViewModel::class.java],
                idViewModel = viewModelProvider[IdViewModel::class.java],
//                savedAnimeViewModel = savedAnimeViewModel
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
//            if (backStackEntry.arguments?.containsKey("id") == true) {
                val id = backStackEntry.arguments!!.getInt("id")
//                val idSaved by idViewModel.mal_id.collectAsStateWithLifecycle()
//                idSaved.value = id
//                idViewModel.setId(id)
                DetailScreen(
                    navController = navController,
             viewModelProvider = viewModelProvider
                )
                Log.d("CATCHED ID = ", id.toString())
//                SendIdToConstraction(mal_id = id)
//            } else {
//                Text(text = "No id provided")
//            }
        }



        composable(route = Screen.Favorites.route) { Fav(navController) } // for the Favorite anime-route
        composable(route = Screen.News.route) { News(navController) }   // for the News route
        composable(route = Nothing.value) { NoId(navController) }
    }
}


//composable(route = NoIdObject.route) { NoId() }
