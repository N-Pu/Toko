package com.example.animeapp.presentation.appConstraction


import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.animeapp.dao.AnimeItem
import com.example.animeapp.dao.MainDb
import com.example.animeapp.domain.viewModel.DetailScreenViewModel
import com.example.animeapp.presentation.navigation.Nothing
import com.example.animeapp.presentation.navigation.Screen
import com.example.animeapp.presentation.navigation.SetupNavGraph
import com.example.animeapp.domain.viewModel.IdViewModel
import com.example.animeapp.presentation.screens.homeScreen.checkIdInDataBase
import com.example.animeapp.presentation.screens.homeScreen.formatScore
import com.example.animeapp.presentation.screens.homeScreen.formatScoredBy
import com.example.animeapp.presentation.theme.LightYellow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material3.BottomAppBar as BottomAppBar


@Composable
fun TokoAppActivator(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
//    savedAnimeViewModel: SavedAnimeViewModel,

) {


    var showButton by remember { mutableStateOf(false) }
    val context = LocalContext.current
    //check if destination matches needed route -> show button
    navController.addOnDestinationChangedListener { _, destination, _ ->
        showButton = when (destination.route) {
            Screen.Detail.route,
//            CharacterDetail.value,
//            StaffDetail.value
            -> true

            else -> false
        }
    }

    Scaffold(bottomBar = {
        BottomNavigationBar(
            navController = navController, idViewModel = viewModelProvider[IdViewModel::class.java]
        )
    }, floatingActionButton = {

        MyFloatingButton(
            showButton = showButton,
            context = context,
            viewModelProvider = viewModelProvider
        )

    }, floatingActionButtonPosition = FabPosition.Center, content = { padding ->
        padding.calculateTopPadding()
        SetupNavGraph(
            navController, viewModelProvider
        )
    })


    LaunchedEffect(showButton) {
        showButton = true
    }

}


@Composable
fun BottomNavigationBar(
    navController: NavController, idViewModel: IdViewModel
) {

    val items = listOf(
        Screen.Home, Screen.Detail, Screen.RandomAnimeOrManga, Screen.Favorites
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        modifier = Modifier.height(36.dp), containerColor = LightYellow
    ) {
        NavigationBarItem(icon = {
            Icon(
                imageVector = items[0].icon, contentDescription = items[0].contentDescription
            )
        }, selected = currentRoute == items[0].route, onClick = {

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
        })

        NavigationBarItem(icon = {
            Icon(
                imageVector = items[1].icon, contentDescription = items[1].contentDescription
            )
        }, selected = currentRoute == items[1].route, onClick = {
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
        })

        NavigationBarItem(icon = {
            Icon(
                imageVector = items[2].icon, contentDescription = items[2].contentDescription
            )
        }, selected = currentRoute == items[2].route, onClick = {

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
        })

        NavigationBarItem(icon = {
            Icon(
                imageVector = items[3].icon, contentDescription = items[3].contentDescription
            )
        }, selected = currentRoute == items[3].route, onClick = {
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
        })

    }
}


@Composable
fun MyFloatingButton(showButton: Boolean, viewModelProvider: ViewModelProvider, context: Context) {
    val items = mutableListOf("Planned", "Watching", "Watched", "Dropped")
    val detailScreenViewModel = viewModelProvider[DetailScreenViewModel::class.java]
    val detailScreenState = viewModelProvider[DetailScreenViewModel::class.java]
        .animeDetails.collectAsStateWithLifecycle()


    val dao = MainDb.getDb(context).getDao()
    if (checkIdInDataBase(dao = dao, id = detailScreenState.value?.mal_id ?: 0)
            .collectAsStateWithLifecycle(initialValue = false).value
    ) {
        items.add(4, "Delete")
    }

    var expanded by remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf("") }

    AnimatedVisibility(
        visible = showButton,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        FloatingActionButton(
            onClick = {
                detailScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
                    expanded = !expanded
                }

            },
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            if (expanded) {
                Icon(Icons.Filled.Close, "Localized description")
            } else {
                Icon(Icons.Filled.Add, "Localized description")
            }
        }
    }

        Box(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .wrapContentHeight()
                .offset(y = (-16).dp) // Сдвигает меню вверх на 16dp, чтобы не перекрывать кнопку
                .padding(end = 16.dp, bottom = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            detailScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
                                selectedItem.value = item
                                if (selectedItem.value == "Delete") {
                                    detailScreenState.value?.let { data ->
                                        dao.removeFromDataBase(data.mal_id)
                                    }
                                } else {
                                    detailScreenState.value?.let { data ->
                                        dao.addToCategory(
                                            AnimeItem(
                                                data.mal_id,
                                                anime = data.title,
                                                score = formatScore(data.score),
                                                scored_by = formatScoredBy(data.scored_by),
                                                animeImage = data.images.jpg.large_image_url,
                                                category = selectedItem.value
                                            )
                                        )
                                    }
                                }
                                // on touched - dropDownMenu cancels
                                expanded = false
                            }
                        },
                        text = {
                            Text(text = item)
                        })
                }
            }
        }

}

@Preview
@Composable
fun Prev() {


    Scaffold(
        bottomBar = {

            Row(
                modifier = Modifier
                    .width(200.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(16.dp))
            ) {


                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Search, contentDescription = ""
                    )
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.List, contentDescription = ""
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart, contentDescription = ""
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder, contentDescription = ""
                    )
                }


            }


//            BottomAppBar(modifier = Modifier) {
//                NavigationBarItem(icon = {
//                    Icon(
//                        imageVector = Icons.Filled.Add, contentDescription = ""
//                    )
//                }, onClick = {}, selected = false)
//                NavigationBarItem(icon = {
//                    Icon(
//                        imageVector = Icons.Filled.Add, contentDescription = ""
//                    )
//                }, onClick = {}, selected = false)
//                NavigationBarItem(icon = {
//                    Icon(
//                        imageVector = Icons.Filled.Add, contentDescription = ""
//                    )
//                }, onClick = {}, selected = false)
//                NavigationBarItem(icon = {
//                    Icon(
//                        imageVector = Icons.Filled.Add, contentDescription = ""
//                    )
//                }, onClick = {}, selected = false)
//            }
        }
    ) {
        it.calculateBottomPadding()


    }
}