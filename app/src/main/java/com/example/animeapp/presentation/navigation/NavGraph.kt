package com.example.animeapp.presentation.navigation


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.animeapp.domain.viewModel.DetailScreenViewModel
import com.example.animeapp.presentation.screens.detailScreen.mainPage.ActivateDetailScreen
import com.example.animeapp.presentation.screens.homeScreen.MainScreen
import com.example.animeapp.presentation.screens.randomAnimeAndManga.ShowRandomScreen
import com.example.animeapp.presentation.screens.noId.NoId
import com.example.animeapp.presentation.screens.detailScreen.sideContent.castList.ShowWholeCast
import com.example.animeapp.presentation.screens.detailScreen.sideContent.staffList.ShowWholeStaff
import com.example.animeapp.presentation.screens.detailScreen.sideContent.characterFull.DisplayCharacterFromId
import com.example.animeapp.presentation.screens.detailScreen.sideContent.producerFull.ShowScreen
import com.example.animeapp.presentation.screens.detailScreen.sideContent.staffMemberFull.DisplayStaffMemberFromId
import com.example.animeapp.presentation.screens.favoritesScreen.FavoriteScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,

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

        composable(route = Nothing.value) {
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
            ShowRandomScreen(
                navController,
                viewModelProvider,
                modifier = modifier
            )
        }
        composable(route = DetailOnCast.value) {
            ShowWholeCast(
                navController, viewModelProvider[DetailScreenViewModel::class.java],
                modifier = modifier
            )
        }
        composable(route = DetailOnStaff.value) {
            ShowWholeStaff(
                navController, viewModelProvider[DetailScreenViewModel::class.java],
                modifier = modifier
            )
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
                viewModelProvider = viewModelProvider,
                modifier = modifier
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
                viewModelProvider = viewModelProvider,
                modifier = modifier
            )
        }

        composable(
            route = ProducerDetail.value,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            }, navArgument("studio_name") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            val studio_name = backStackEntry.arguments!!.getString("studio_name")!!
            ShowScreen(
                id = id, viewModelProvider = viewModelProvider, studio_name = studio_name,
                modifier = modifier
            )
        }

    }
}