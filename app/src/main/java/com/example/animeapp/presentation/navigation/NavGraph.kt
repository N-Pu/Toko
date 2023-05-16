package com.example.animeapp.presentation.navigation


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.animeapp.presentation.Screens.favoritesScreen.Fav
import com.example.animeapp.presentation.Screens.homeScreen.MainScreen
import com.example.animeapp.presentation.Screens.newsScreen.News
import com.example.animeapp.presentation.Screens.noId.NoId
import com.example.animeapp.presentation.detailScreen.DetailScreen
import com.example.animeapp.presentation.Screens.detailScreen.sideContent.castList.ShowWholeCast
import com.example.animeapp.presentation.detailScreen.castList.ShowWholeStaff
import com.example.animeapp.presentation.Screens.detailScreen.sideContent.characterFull.DisplayCharacterFromId
import com.example.animeapp.presentation.Screens.detailScreen.sideContent.staffMemberFull.DisplayStaffMemberFromId


//@Composable
//fun SetupNavGraph(
//    navController: NavHostController,
//    viewModelProvider: ViewModelProvider
//) {
//    NavHost(navController = navController, startDestination = Screen.Home.route) {
//
//        composable(route = Screen.Home.route) {
//            MainScreen(
//                navController = navController,
//                viewModelProvider = viewModelProvider,
//            )
//        }
//
//        composable(
//            route = Screen.Detail.route,
//            arguments = listOf(navArgument("id") {
//                type = NavType.IntType
//            })
//        ) { backStackEntry ->
//            val id = backStackEntry.arguments!!.getInt("id")
//            DetailScreen(
//                navController = navController,
//                viewModelProvider = viewModelProvider
//            )
//            Log.d("CATCHED ID = ", id.toString())
//
//        }
//
//
//
//        composable(route = Screen.Favorites.route) { Fav(navController) } // for the Favorite anime-route
//        composable(route = Screen.News.route) { News(navController) }   // for the News route
//        composable(route = Nothing.value) { NoId(navController) }
//
//        composable(route = DetailOnCast.value) {
//            ShowWholeCast(navController, viewModelProvider)
//        }
//        composable(route = DetailOnStaff.value) {
//            ShowWholeStaff(navController, viewModelProvider)
//        }
//
//        composable(
//            route = CharacterDetail.value,
//            arguments = listOf(navArgument("id") {
//                type = NavType.IntType
//            })
//        ) { backStackEntry ->
//
//            val id = backStackEntry.arguments!!.getInt("id")
//            DisplayCharacterFromId(mal_id = id, navController = navController, viewModelProvider = viewModelProvider)
//        }
//
//        composable(
//            route = StaffDetail.value,
//            arguments = listOf(navArgument("id") {
//                type = NavType.IntType
//            })
//        ) { backStackEntry ->
//
//            val id = backStackEntry.arguments!!.getInt("id")
//            DisplayStaffMemberFromId(mal_id = id, navController = navController, viewModelProvider = viewModelProvider)
//        }
//    }
//}

@Composable
fun SetupNavGraph(navController: NavHostController, viewModelProvider: ViewModelProvider) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            MainScreen(navController = navController, viewModelProvider = viewModelProvider)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            DetailScreen(navController = navController, viewModelProvider = viewModelProvider)
            Log.d("CATCHED ID = ", id.toString())
        }
        composable(route = Screen.Favorites.route) {
            Fav(
                navController = navController,
                viewModelProvider = viewModelProvider
            )
        }
        composable(route = Screen.News.route) { News(navController) }
        composable(route = Nothing.value) { NoId(navController) }
        composable(route = DetailOnCast.value) {
            ShowWholeCast(navController, viewModelProvider)
        }
        composable(route = DetailOnStaff.value) {
            ShowWholeStaff(navController, viewModelProvider)
        }
        composable(
            route = CharacterDetail.value,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            DisplayCharacterFromId(
                mal_id = id,
                navController = navController,
                viewModelProvider = viewModelProvider
            )
        }
        composable(
            route = StaffDetail.value,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            DisplayStaffMemberFromId(
                mal_id = id,
                navController = navController,
                viewModelProvider = viewModelProvider
            )
        }
    }
}