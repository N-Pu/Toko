package com.project.toko.core.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import coil.ImageLoader
import com.project.toko.detailScreen.ui.viewModel.DetailScreenViewModel
import com.project.toko.detailScreen.ui.detailScreen.mainPage.ActivateDetailScreen
import com.project.toko.homeScreen.ui.homeScreen.MainScreen
import com.project.toko.detailScreen.ui.detailScreen.sideContent.castList.ShowWholeCast
import com.project.toko.detailScreen.ui.detailScreen.sideContent.staffList.ShowWholeStaff
import com.project.toko.characterDetailedScreen.ui.characterFull.DisplayCharacterFromId
import com.project.toko.daoScreen.ui.screen.DaoScreen
import com.project.toko.personDetailedScreen.ui.staffMemberFull.DisplayPersonFullScreen
import com.project.toko.randomAnimeScreen.presentation_layer.randomAnimeScreen.ShowRandomAnime

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    isInDarkTheme: () -> Boolean,
    drawerState: DrawerState,
    svgImageLoader: ImageLoader
) {
    NavHost(navController = navController, startDestination = RootScreen.HomeSubGraph.route) {

        homeSubGraph(navController, drawerState, svgImageLoader, isInDarkTheme)
        daoSubGraph(navController, drawerState, svgImageLoader, isInDarkTheme)
        randomSubGraph(navController, svgImageLoader, isInDarkTheme)

    }
}

private fun NavGraphBuilder.homeSubGraph(
    navController: NavController, drawerState: DrawerState,
    svgImageLoader: ImageLoader, isInDarkTheme: () -> Boolean,
) {
    // Main Graph
    navigation(startDestination = LeafScreen.Home.route, route = RootScreen.HomeSubGraph.route) {
        composable(route = LeafScreen.Home.route) {
            MainScreen(
                onNavigateToDetailScreen = { detailScreenId ->
                    navController.navigate("detail_screen_home/$detailScreenId") {
                        launchSingleTop = true
                    }
                },
                isInDarkTheme = isInDarkTheme,
                drawerState = drawerState,
                svgImageLoader = svgImageLoader
            )
        }
        composable(route = LeafScreen.DetailHome.route, arguments = listOf(navArgument("id") {
            type = NavType.IntType
        })) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            ActivateDetailScreen(onNavigateToDetailOnWholeCast = { id ->
                navController.navigate("detail_on_whole_cast_home/$id")
            },
                onNavigateToDetailOnCharacter = { characterId ->
                    navController.navigate("detail_on_character_home/$characterId")
                }, onNavigateToDetailOnStaff = { personId ->
                    navController.navigate("detail_on_staff_home/$personId")
                }, onNavigateToWholeOnStaff = {
                    navController.navigate("detail_on_whole_staff_home")
                }, onNavigateToDetailScreen = { detailScreenId ->
                    navController.navigate("detail_screen_home/$detailScreenId") {
                        launchSingleTop = true
                    }
                },
                id = id,
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader
            )
        }
        composable(route = LeafScreen.DetailOnWholeCastHome.route) {
            val detailScreenViewModel: DetailScreenViewModel = hiltViewModel()
            ShowWholeCast(
                onNavigateToDetailOnCharacter = { characterId ->
                    navController.navigate("detail_on_character_home/$characterId")
                }, onNavigateToDetailOnStaff = { personId ->
                    navController.navigate("detail_on_staff_home/$personId")
                },
                onNavigateBack = { navController.navigateUp() },
                detailScreenViewModel,
                isInDarkTheme = isInDarkTheme
            )
        }
        composable(route = LeafScreen.DetailOnWholeStaffHome.route) {
            val detailScreenViewModel: DetailScreenViewModel = hiltViewModel()
            ShowWholeStaff(
                onNavigateToDetailOnStaff = { personId ->
                    navController.navigate("detail_on_staff_home/$personId")
                },
                onNavigateBack = { navController.navigateUp() },
                detailScreenViewModel,
                isInDarkTheme = isInDarkTheme
            )
        }
        composable(
            route = LeafScreen.CharacterDetailHome.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            DisplayCharacterFromId(
                id = id,
                onNavigateToStaff = { staffId ->
                    navController.navigate("detail_on_staff_home/$staffId")
                },
                onNavigateToDetailScreen = { detailScreenId ->
                    navController.navigate("detail_screen_home/$detailScreenId") {
                        launchSingleTop = true
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                },
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader
            )
        }
        composable(route = LeafScreen.StaffDetailHome.route, arguments = listOf(navArgument("id") {
            type = NavType.IntType
        })) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            DisplayPersonFullScreen(
                id = id,
                onNavigateToDetailOnCharacter = { characterId ->
                    navController.navigate("detail_on_character_home/$characterId")
                }, onNavigateToDetailScreen = { detailScreenId ->
                    navController.navigate("detail_screen_home/$detailScreenId") {
                        launchSingleTop = true
                    }
                },
                onNavigateBack = { navController.navigateUp() },
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader
            )
        }
    }
}

private fun NavGraphBuilder.daoSubGraph(
    navController: NavController, drawerState: DrawerState,
    svgImageLoader: ImageLoader, isInDarkTheme: () -> Boolean,
): Unit {
    // Dao Graph
    navigation(
        startDestination = LeafScreen.Favorites.route,
        route = RootScreen.DaoSubGraph.route
    ) {
        composable(route = LeafScreen.Favorites.route) {
            DaoScreen(
                onNavigateToDetailOnCharacter = { characterId ->
                    navController.navigate("detail_on_character_dao/$characterId")
                }, onNavigateToDetailOnStaff = { personId ->
                    navController.navigate("detail_on_staff_dao/$personId")
                }, onNavigateToDetailScreen = { detailScreenId ->
                    navController.navigate("detail_screen_dao/$detailScreenId") {
                        launchSingleTop = true
                    }
                },
                isInDarkTheme = isInDarkTheme,
                drawerState = drawerState,
                svgImageLoader = svgImageLoader
            )
        }
        composable(route = LeafScreen.DetailDao.route, arguments = listOf(navArgument("id") {
            type = NavType.IntType
        })) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            ActivateDetailScreen(onNavigateToDetailOnWholeCast = { id ->
                navController.navigate("detail_on_whole_cast_dao/$id")
            },
                onNavigateToDetailOnCharacter = { characterId ->
                    navController.navigate("detail_on_character_dao/$characterId")
                }, onNavigateToDetailOnStaff = { personId ->
                    navController.navigate("detail_on_staff_dao/$personId")
                }, onNavigateToWholeOnStaff = {
                    navController.navigate("detail_on_whole_staff_dao")
                }, onNavigateToDetailScreen = { detailScreenId ->
                    navController.navigate("detail_screen_dao/$detailScreenId") {
                        launchSingleTop = true
                    }
                },
                id = id,
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader
            )
        }
        composable(route = LeafScreen.DetailOnWholeCastDao.route) {
            val detailScreenViewModel: DetailScreenViewModel = hiltViewModel()
            ShowWholeCast(
                onNavigateToDetailOnCharacter = { characterId ->
                    navController.navigate("detail_on_character_dao/$characterId")
                }, onNavigateToDetailOnStaff = { personId ->
                    navController.navigate("detail_on_staff_dao/$personId")
                },
                onNavigateBack = { navController.navigateUp() },
                detailScreenViewModel,
                isInDarkTheme = isInDarkTheme
            )
        }
        composable(route = LeafScreen.DetailOnWholeStaffDao.route) {
            val detailScreenViewModel: DetailScreenViewModel = hiltViewModel()
            ShowWholeStaff(
                onNavigateToDetailOnStaff = { personId ->
                    navController.navigate("detail_on_staff_dao/$personId")
                },
                onNavigateBack = { navController.navigateUp() },
                detailScreenViewModel,
                isInDarkTheme = isInDarkTheme
            )
        }
        composable(
            route = LeafScreen.CharacterDetailDao.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            DisplayCharacterFromId(
                id = id,
                onNavigateToStaff = { staffId ->
                    navController.navigate("detail_on_staff_dao/$staffId")
                },
                onNavigateToDetailScreen = { detailScreenId ->
                    navController.navigate("detail_screen_dao/$detailScreenId") {
                        launchSingleTop = true
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                },
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader
            )
        }
        composable(route = LeafScreen.StaffDetailDao.route, arguments = listOf(navArgument("id") {
            type = NavType.IntType
        })) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            DisplayPersonFullScreen(
                id = id,
                onNavigateToDetailOnCharacter = { characterId ->
                    navController.navigate("detail_on_character_dao/$characterId")
                }, onNavigateToDetailScreen = { detailScreenId ->
                    navController.navigate("detail_screen_dao/$detailScreenId") {
                        launchSingleTop = true
                    }
                },
                onNavigateBack = { navController.navigateUp() },
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader
            )
        }
    }
}

fun NavGraphBuilder.randomSubGraph(
    navController: NavController,
    svgImageLoader: ImageLoader, isInDarkTheme: () -> Boolean,
) {
    // Random Anime Graph
    navigation(
        startDestination = LeafScreen.RandomAnimeOrManga.route,
        route = RootScreen.RandomSubGraph.route
    ) {
        composable(route = LeafScreen.RandomAnimeOrManga.route) {
            ShowRandomAnime(
                onNavigateToDetailScreen = { detailScreenId ->
                    navController.navigate("detail_screen_random/$detailScreenId")
//                    {
//                        launchSingleTop = true
//                    }
                },
            )
        }
        composable(route = LeafScreen.DetailRandom.route, arguments = listOf(navArgument("id") {
            type = NavType.IntType
        })) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            ActivateDetailScreen(onNavigateToDetailOnWholeCast = { id ->
                navController.navigate("detail_on_whole_cast_random/$id")
            },
                onNavigateToDetailOnCharacter = { characterId ->
                    navController.navigate("detail_on_character_random/$characterId")
                }, onNavigateToDetailOnStaff = { personId ->
                    navController.navigate("detail_on_staff_random/$personId")
                }, onNavigateToWholeOnStaff = {
                    navController.navigate("detail_on_whole_staff_random")
                }, onNavigateToDetailScreen = { detailScreenId ->
                    navController.navigate("detail_screen_random/$detailScreenId")
//                    {
//                        launchSingleTop = true
//                    }
                },
                id = id,
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader
            )
        }
        composable(route = LeafScreen.DetailOnWholeCastRandom.route) {
            val detailScreenViewModel: DetailScreenViewModel = hiltViewModel()
            ShowWholeCast(
                onNavigateToDetailOnCharacter = { characterId ->
                    navController.navigate("detail_on_character_random/$characterId")
                }, onNavigateToDetailOnStaff = { personId ->
                    navController.navigate("detail_on_staff_random/$personId")
                },
                onNavigateBack = { navController.navigateUp() },
                detailScreenViewModel,
                isInDarkTheme = isInDarkTheme
            )
        }
        composable(route = LeafScreen.DetailOnWholeStaffRandom.route) {
            val detailScreenViewModel: DetailScreenViewModel = hiltViewModel()
            ShowWholeStaff(
                onNavigateToDetailOnStaff = { personId ->
                    navController.navigate("detail_on_staff_random/$personId")
                },
                onNavigateBack = { navController.navigateUp() },
                detailScreenViewModel,
                isInDarkTheme = isInDarkTheme
            )
        }
        composable(
            route = LeafScreen.CharacterDetailRandom.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            DisplayCharacterFromId(
                id = id,
                onNavigateToStaff = { staffId ->
                    navController.navigate("detail_on_staff_random/$staffId")
                },
                onNavigateToDetailScreen = { detailScreenId ->
                    navController.navigate("detail_screen_random/$detailScreenId")
//                    {
//                        launchSingleTop = true
//                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                },
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader
            )
        }
        composable(
            route = LeafScreen.StaffDetailRandom.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            DisplayPersonFullScreen(
                id = id,
                onNavigateToDetailOnCharacter = { characterId ->
                    navController.navigate("detail_on_character_random/$characterId")
                }, onNavigateToDetailScreen = { detailScreenId ->
                    navController.navigate("detail_screen_random/$detailScreenId")
//                    {
//                        launchSingleTop = true
//                    }
                },
                onNavigateBack = { navController.navigateUp() },
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader
            )
        }
    }
}
