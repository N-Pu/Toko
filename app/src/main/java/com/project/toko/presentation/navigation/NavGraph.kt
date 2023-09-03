package com.project.toko.presentation.navigation


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.toko.dao.MainDb
import com.project.toko.domain.viewModel.DetailScreenViewModel
import com.project.toko.presentation.screens.detailScreen.mainPage.ActivateDetailScreen
import com.project.toko.presentation.screens.homeScreen.MainScreen
import com.project.toko.presentation.screens.randomAnimeScreen.ShowRandomScreen
import com.project.toko.presentation.screens.noId.NoId
import com.project.toko.presentation.screens.detailScreen.sideContent.castList.ShowWholeCast
import com.project.toko.presentation.screens.detailScreen.sideContent.staffList.ShowWholeStaff
import com.project.toko.presentation.screens.detailScreen.sideContent.characterFull.DisplayCharacterFromId
import com.project.toko.presentation.screens.detailScreen.sideContent.producerFull.ShowScreen
import com.project.toko.presentation.screens.detailScreen.sideContent.staffMemberFull.DisplayStaffMemberFromId
import com.project.toko.presentation.screens.favoritesScreen.FavoriteScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,

    ) {
    val dao = MainDb.getDb(LocalContext.current).getDao()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            MainScreen(
                navController = navController,
                viewModelProvider = viewModelProvider,
                modifier = modifier,
                dao = dao
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

        composable(route = Screen.Nothing.value) {
            NoId()
        }
        composable(route = Screen.Favorites.route) {
            FavoriteScreen(
                navController = navController,
                viewModelProvider = viewModelProvider,
                modifier = modifier,
                dao = dao
            )
        }
        composable(route = Screen.RandomAnimeOrManga.route) {
            ShowRandomScreen(
                navController,
                viewModelProvider,
                modifier = modifier,
                dao = dao
            )
        }
        composable(route = Screen.DetailOnCast.value) {
            ShowWholeCast(
                navController, viewModelProvider[DetailScreenViewModel::class.java],
                modifier = modifier
            )
        }
        composable(route = Screen.DetailOnStaff.value) {
            ShowWholeStaff(
                navController, viewModelProvider[DetailScreenViewModel::class.java],
                modifier = modifier
            )
        }
        composable(
            route = Screen.CharacterDetail.value,
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
            route = Screen.StaffDetail.value,
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
            route = Screen.ProducerDetail.value,
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