package com.example.animeapp.presentation.appConstraction


import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.animeapp.presentation.navigation.Nothing
import com.example.animeapp.presentation.navigation.Screen
import com.example.animeapp.presentation.navigation.SetupNavGraph
import com.example.animeapp.viewModel.IdViewModel

import androidx.compose.material3.BottomAppBar as BottomAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokoAppActivator(
    navController: NavHostController,
 viewModelProvider: ViewModelProvider
//    savedAnimeViewModel: SavedAnimeViewModel,

) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                idViewModel = viewModelProvider[IdViewModel::class.java]
            )
        },
        floatingActionButton = { MyFloatingButton(navController = navController) }
            , floatingActionButtonPosition = FabPosition.Center
        ,
//        topBar = { MyTopAppBar(navController = navController, idViewModel = idViewModel) },
        content = { padding ->
            padding.calculateTopPadding()
            SetupNavGraph(
                navController,
            viewModelProvider
//                savedAnimeViewModel
            )
        })


}

//@Composable
//fun MyTopAppBar(navController: NavController, idViewModel: IdViewModel) {
////    val currentRoute = navController.currentDestination?.route
////    val isDetailScreen = currentRoute == Screen.Detail.route
//
//    IconButton(
//        onClick = {
//            navController.navigate(route = Screen.Home.route) {
//                popUpTo(Screen.Home.route) {
//                    inclusive = true
//                }
////                idViewModel.setId(0)
//            }
//        }) {
//        Icon(
//            imageVector = Icons.Filled.ArrowBack,
//            contentDescription = "Back to Search"
//        )
//    }
//}
//

//@Composable
//fun MyTopAppBar(navController: NavController, idViewModel: IdViewModel) {
//    val currentRoute = navController.currentDestination?.route
//    val isDetailScreen = currentRoute == Screen.Detail.route
//
//    if (isDetailScreen) {
//        IconButton(
//            onClick = {
//                navController.navigate(route = Screen.Home.route) {
//                    popUpTo(Screen.Home.route) {
//                        inclusive = true
//                    }
//                }
//            }) {
//            Icon(
//                imageVector = Icons.Filled.ArrowBack,
//                contentDescription = "Back to Search"
//            )
//        }
//    }
//}




@Composable
fun MyFloatingButton(navController: NavController) {
    FloatingActionButton(
        onClick = {

        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Icon(Icons.Filled.Add, "Localized description")
    }
}


@Composable
fun BottomNavigationBar(
    navController: NavController,
    idViewModel: IdViewModel
) {

    val items = listOf(
        Screen.Home,
        Screen.Detail,
        Screen.News,
        Screen.Favorites,
//        Nothing.value
    )

//    val id by idViewModel.mal_id.collectAsStateWithLifecycle()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    BottomAppBar {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = items[0].icon,
                    contentDescription = items[0].contentDescription
                )
            },
            selected = currentRoute == items[0].route,
            onClick = {

                if (currentRoute != null) {
                    Log.d("currentRoute", currentRoute + "==" + items[0].route)

                }

                try {
                    navController.navigate(items[0].route) {
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true

                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
//                                    inclusive = true
                            }
                            restoreState = true
                            // Restore state when reselecting a previously selected item
                        }

                    }
                } catch (e: IllegalArgumentException) {

                    Log.e("CATCH", items[0].route + " " + e.message.toString())

                }
            }

        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = items[1].icon,
                    contentDescription = items[1].contentDescription
                )
            },
            selected = currentRoute == items[1].route,
            onClick = {
                try {
                    if (idViewModel.getId() == 0) {
                        navController.navigate(Nothing.value) {
                            navController.graph.startDestinationRoute?.let { route ->
                                launchSingleTop = true
                                popUpTo(route) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                        }
                    } else {
                        navController.navigate("detail_screen/${idViewModel.getId()}") {
                            launchSingleTop = true
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            restoreState = true
                        }
                    }

                } catch (e: IllegalArgumentException) {
                    Log.e("CATCH", items[1].route + " " + e.message.toString())
                }
            }

        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = items[2].icon,
                    contentDescription = items[2].contentDescription
                )
            },
            selected = currentRoute == items[2].route,
            onClick = {

                try {
                    navController.navigate(items[2].route) {
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        navController.graph.startDestinationRoute?.let { route ->
                            Log.d(
                                " navController.graph.startDestinationRoute ",
                                navController.graph.startDestinationRoute!!
                            )
                            popUpTo(route) {
                                saveState = true
//                                    inclusive = true
                            }
                        }

                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                } catch (e: IllegalArgumentException) {

                    Log.e("CATCH", items[2].route + " " + e.message.toString())

                }
            }

        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = items[3].icon,
                    contentDescription = items[3].contentDescription
                )
            },
            selected = currentRoute == items[3].route,
            onClick = {

                try {
                    navController.navigate(items[3].route) {
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        navController.graph.startDestinationRoute?.let { route ->
                            Log.d(
                                " navController.graph.startDestinationRoute ",
                                navController.graph.startDestinationRoute!!
                            )
                            popUpTo(route) {
                                saveState = true
//                                    inclusive = true
                            }
                        }

                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                } catch (e: IllegalArgumentException) {

                    Log.e("CATCH", items[3].route + " " + e.message.toString())

                }
            }

        )

//        items.forEach { item ->
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        imageVector = item.icon,
//                        contentDescription = item.contentDescription
//                    )
//                },
//                selected = currentRoute == item.route,
//                onClick = {
//
//                    try {
//
//                        if (item.route == "detail_screen/{id}") {
//                            item.route = "detail_screen"
//                        }
//                            navController.navigate(item.route) {
//                                navController.graph.startDestinationRoute?.let { route ->
//                                    popUpTo(route) {
//                                        saveState = true
////                                    inclusive = true
//                                    }
//                                }
//                                // Avoid multiple copies of the same destination when
//                                // reselecting the same item
//                                launchSingleTop = true
//                                // Restore state when reselecting a previously selected item
//                                restoreState = true
//                            }
//
//
//
//
//                    } catch (e: IllegalArgumentException) {
//
//                        Log.e("CATCH", item.route + " " + e.message.toString())
//
//                    }
//                }
//
//            )
//        }
    }
}

//@Preview
//@Composable
//fun Prev() {
//    val navController = rememberNavController()
//    TokoAppActivator(navController)
//
//}


