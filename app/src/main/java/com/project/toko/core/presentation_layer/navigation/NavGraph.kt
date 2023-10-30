package com.project.toko.core.presentation_layer.navigation


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.ActivateDetailScreen
import com.project.toko.homeScreen.presentation_layer.homeScreen.MainScreen
import com.project.toko.noId.presentation_layer.noId.NoId
import com.project.toko.detailScreen.presentation_layer.detailScreen.sideContent.castList.ShowWholeCast
import com.project.toko.detailScreen.presentation_layer.detailScreen.sideContent.staffList.ShowWholeStaff
import com.project.toko.characterDetailedScreen.presentation_layer.characterFull.DisplayCharacterFromId
import com.project.toko.producerDetailedScreen.presentation_layer.producerFull.ShowScreen
import com.project.toko.staffDetailedScreen.presentation_layer.staffMemberFull.DisplayStaffMemberFromId
import com.project.toko.favoritesScreen.presentation_layer.FavoriteScreen
import com.project.toko.randomAnimeScreen.presentation_layer.randomAnimeScreen.ShowRandomAnime

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            MainScreen(
                navController = navController,
                viewModelProvider = viewModelProvider,
                modifier = modifier
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            ActivateDetailScreen(
                navController = navController,
                viewModelProvider = viewModelProvider,
                id = id,
                modifier = modifier
            )
            Log.d("CATCHED ID = ", id.toString())
        }

        composable(route = Screen.Nothing.route) {
            NoId()
        }
        composable(route = Screen.Favorites.route) {
            FavoriteScreen(
                navController = navController,
                viewModelProvider = viewModelProvider,
                modifier = modifier
            )
        }
        composable(route = Screen.RandomAnimeOrManga.route) {
            ShowRandomAnime(
                navController = navController,
                viewModelProvider = viewModelProvider,
                modifier = modifier,
            )
        }
        composable(route = Screen.DetailOnWholeCast.route) {

            ShowWholeCast(
                navController,
                viewModelProvider[DetailScreenViewModel::class.java],
                modifier
            )
        }
        composable(route = Screen.DetailOnWholeStaff.route) {
            ShowWholeStaff(
                navController,
                viewModelProvider[DetailScreenViewModel::class.java],
                modifier = modifier
            )
        }
        composable(
            route = Screen.CharacterDetail.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            DisplayCharacterFromId(
                mal_id = id,
                navController = navController,
                viewModelProvider = viewModelProvider,
                modifier = modifier
            )
        }
        composable(
            route = Screen.StaffDetail.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            DisplayStaffMemberFromId(
                mal_id = id,
                navController = navController,
                viewModelProvider = viewModelProvider,
                modifier = modifier
            )
        }
        composable(
            route = Screen.ProducerDetail.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            }
//                , navArgument("studio_name") {
//                type = NavType.StringType
//
//                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
//            val studio_name = backStackEntry.arguments!!.getString("studio_name")!!
            ShowScreen(
                id = id, viewModelProvider = viewModelProvider,

//                studio_name = studio_name,
                modifier = modifier
            )
        }

    }
}

//@Composable
//inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
//    val navGraphRoute = destination.parent?.route ?: return viewModel()
//    val parentEntry = remember(this) {
//        navController.getBackStackEntry(navGraphRoute)
//    }
//    return viewModel(parentEntry)
//}