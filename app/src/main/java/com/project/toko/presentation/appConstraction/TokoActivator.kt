package com.project.toko.presentation.appConstraction


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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.project.toko.dao.AnimeItem
import com.project.toko.dao.MainDb
import com.project.toko.domain.viewModel.DetailScreenViewModel
import com.project.toko.presentation.navigation.Nothing
import com.project.toko.presentation.navigation.Screen
import com.project.toko.presentation.navigation.SetupNavGraph
import com.project.toko.presentation.screens.homeScreen.checkIdInDataBase
import com.project.toko.presentation.screens.homeScreen.formatScore
import com.project.toko.presentation.screens.homeScreen.formatScoredBy
import com.project.toko.presentation.theme.LightGreen
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
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f)
                .padding(bottom = 45.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                MyFloatingButton(
                    showButton = showButton,
                    context = context,
                    viewModelProvider = viewModelProvider,
                    modifier = modifier
                )
            }

            Row {
                BottomNavigationBar(
                    navController = navController,
                    currentDetailScreenId = currentDetailScreenId,
                    modifier = modifier
                )

            }


        }
    },
//        floatingActionButton = {},
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
}


@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentDetailScreenId: MutableState<Int>,
    modifier: Modifier
) {


    val items = listOf(
        Screen.Home, Screen.Detail, Screen.Favorites, Screen.RandomAnimeOrManga
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = modifier
            .clip(
                RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)
            )
            .fillMaxWidth(1f)
//            .width(240.dp)
//            .shadow(20.dp)
            .background(LightGreen.copy(alpha = 0.6f))
            .background(Color.Transparent)
            .height(50.dp),

        horizontalArrangement = Arrangement.SpaceAround,
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
                imageVector = ImageVector.vectorResource(id = items[0].iconId),
                contentDescription = items[0].contentDescription,
                modifier = modifier
                    .size(30.dp)
                    .clickable {
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
                imageVector = ImageVector.vectorResource(id = items[1].iconId),
                contentDescription = items[1].contentDescription,
                modifier = modifier
                    .size(30.dp)
                    .clickable {
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
                imageVector = ImageVector.vectorResource(id = items[2].iconId),
                contentDescription = items[2].contentDescription,
                modifier = modifier
                    .size(30.dp)
                    .clickable {
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
                imageVector = ImageVector.vectorResource(id = items[3].iconId),
                contentDescription = items[3].contentDescription,
                modifier = modifier
                    .size(30.dp)
                    .clickable {
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

//    }
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

        Box(
            modifier = modifier
                .height(70.dp)
                .width(70.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = {
                    detailScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
                        expanded = !expanded
                    }

                },
                containerColor = LightGreen.copy(alpha = 0.6f),
                modifier = modifier
                    .height(50.dp)
                    .width(50.dp)
            ) {
                if (expanded) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Localized description"
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Localized description"
                    )
                }
            }

//            Box(modifier = modifier
//                .shadow(20.dp)
//                .clip(RoundedCornerShape(10.dp))
//                .clickable {
//                    detailScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
//                        expanded = !expanded
//                    }
//                }
//                .background(LightGreen.copy(alpha = 0.6f))
//                .height(50.dp)
//                .width(50.dp),
//                contentAlignment = Alignment.Center) {
//
//            }
//            if (expanded) {
//                Icon(
//                    modifier = Modifier.size(30.dp),
//                    imageVector = Icons.Filled.Close,
//                    contentDescription = "Localized description"
//                )
//            } else {
//                Icon(
//                    modifier = Modifier.size(30.dp),
//                    imageVector = Icons.Filled.Add,
//                    contentDescription = "Localized description"
//                )
//            }
        }
    }

//    Box(
//        modifier = modifier
//            .width(IntrinsicSize.Min)
//            .wrapContentHeight()
//            .offset(x = (-110).dp, y = (-100).dp)
//            .padding(end = 0.dp, bottom = 16.dp),
//        contentAlignment = Alignment.BottomEnd
//    ) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = modifier
            .background(LightGreen),
        offset = DpOffset(x = (20).dp, y = (-250).dp)
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
//    }

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