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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.animeapp.presentation.screens.homeScreen.checkIdInDataBase
import com.example.animeapp.presentation.screens.homeScreen.formatScore
import com.example.animeapp.presentation.screens.homeScreen.formatScoredBy
import com.example.animeapp.presentation.theme.LightGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun TokoAppActivator(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    context: Context,
    modifier: Modifier
) {

    val currentDetailScreenId = viewModelProvider[DetailScreenViewModel::class.java].loadedId
    var showButton by remember { mutableStateOf(false) }

    navController.addOnDestinationChangedListener { _, destination, arguments ->
        showButton = when (destination.route) {
            Screen.Detail.route -> {
                currentDetailScreenId.value = arguments?.getInt("id") ?: 0

                true
            }

            else -> false
        }
    }

    Scaffold(bottomBar = {
        BottomNavigationBar(
            navController = navController,
            currentDetailScreenId = currentDetailScreenId,
            modifier = modifier
        )
    }, floatingActionButton = {

        MyFloatingButton(
            showButton = showButton,
            context = context,
            viewModelProvider = viewModelProvider,
            modifier = modifier
        )

    },
        snackbarHost = {

            // нижний бар уведомлений
//            Snackbar {
//                Box(
//                    Modifier
//                        .fillMaxSize()
//                        .background(Color.Red)) {
//                        Text("TEXT")
//                }
//            }

        }, floatingActionButtonPosition = FabPosition.Center,
        content = { padding ->
            padding.calculateTopPadding()
            SetupNavGraph(
                navController = navController, viewModelProvider = viewModelProvider,
                modifier = modifier,
            )
        }
    )


    LaunchedEffect(showButton) {
        showButton = true
    }

}


@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentDetailScreenId: MutableState<Int>,
    modifier: Modifier
) {


    val items = listOf(
        Screen.Home, Screen.Detail, Screen.RandomAnimeOrManga, Screen.Favorites
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(1f)
            .padding(bottom = 60.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = modifier
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .fillMaxWidth(0.5f)
                .background(LightGreen.copy(alpha = 0.6f))
                .background(Color.Transparent)
                .height(50.dp)
                .padding(horizontal = 0.dp, vertical = 0.dp),

            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (currentRoute != null) {
                Log.d("currentRoute", currentRoute + "==" + items[0].route)

            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Icon(
                    imageVector = items[0].icon,
                    contentDescription = items[0].contentDescription,
                    modifier = modifier.clickable {
                        try {
                            navController.navigate(items[0].route) {

                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                navController.graph.startDestinationRoute?.let { _ ->
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                }

                            }
                        } catch (e: IllegalArgumentException) {

                            Log.e("CATCH", items[0].route + " " + e.message.toString())

                        }
                    }
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Icon(
                    imageVector = items[1].icon,
                    contentDescription = items[1].contentDescription,
                    modifier = modifier.clickable {
                        try {
                            if (currentDetailScreenId.value != 0) {
                                navController.navigate("detail_screen/${currentDetailScreenId.value}") {
                                    navController.graph.startDestinationRoute?.let { _ ->
                                        launchSingleTop = true
                                    }
                                }
                            } else {
                                navController.navigate(Nothing.value) {
                                    launchSingleTop = true
                                }
                            }

                        } catch (e: IllegalArgumentException) {
                            Log.e("CATCH", items[1].route + " " + e.message.toString())
                        }
                    }
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Icon(
                    imageVector = items[2].icon,
                    contentDescription = items[2].contentDescription,
                    modifier = modifier.clickable {
                        try {
                            navController.navigate(items[2].route) {

                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                navController.graph.startDestinationRoute?.let { _ ->
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                }

                            }
                        } catch (e: IllegalArgumentException) {

                            Log.e("CATCH", items[2].route + " " + e.message.toString())

                        }
                    }
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Icon(
                    imageVector = items[3].icon,
                    contentDescription = items[3].contentDescription,
                    modifier = modifier.clickable {
                        try {
                            navController.navigate(items[3].route) {

                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                navController.graph.startDestinationRoute?.let { _ ->
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                }

                            }
                        } catch (e: IllegalArgumentException) {

                            Log.e("CATCH", items[3].route + " " + e.message.toString())

                        }
                    }
                )
            }
        }

    }
}


@Composable
fun MyFloatingButton(
    showButton: Boolean,
    viewModelProvider: ViewModelProvider,
    context: Context,
    modifier: Modifier
) {
    val items = mutableListOf("Planned", "Watching", "Watched", "Dropped")
    val detailScreenViewModel = viewModelProvider[DetailScreenViewModel::class.java]
    val detailScreenState by viewModelProvider[DetailScreenViewModel::class.java]
        .animeDetails.collectAsStateWithLifecycle()


    val dao = MainDb.getDb(context).getDao()
    if (checkIdInDataBase(dao = dao, id = detailScreenState?.mal_id ?: 0)
            .collectAsStateWithLifecycle(initialValue = false).value
    ) {
        items.add(4, "Delete")
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("") }

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

        Column(
            modifier = Modifier.fillMaxSize(1f)
        ) {

            FloatingActionButton(
                onClick = {
                    detailScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
                        expanded = !expanded
                    }

                },
                containerColor = MaterialTheme.colorScheme.secondary,
                modifier = modifier
                    .background(Color.Red)
//                    .size(200.dp)
//                    .padding(bottom = 30.dp)
            ) {
                if (expanded) {
                    Icon(Icons.Filled.Close, "Localized description")
                } else {
                    Icon(Icons.Filled.Add, "Localized description")
                }
            }
        }
    }

    Box(
        modifier = modifier
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
                            selectedItem = item
                            if (selectedItem == "Delete") {
                                detailScreenState?.let { data ->
                                    dao.removeFromDataBase(data.mal_id)
                                }
                            } else {
                                detailScreenState?.let { data ->
                                    dao.addToCategory(
                                        AnimeItem(
                                            data.mal_id,
                                            anime = data.title,
                                            score = formatScore(data.score),
                                            scored_by = formatScoredBy(data.scored_by),
                                            animeImage = data.images.jpg.large_image_url,
                                            category = selectedItem
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
        }
    ) {
        it.calculateBottomPadding()


    }
}