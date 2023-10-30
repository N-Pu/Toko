package com.project.toko.core.presentation_layer.appConstraction

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.project.toko.R
import com.project.toko.characterDetailedScreen.viewModel.CharacterFullByIdViewModel
import com.project.toko.core.presentation_layer.backArrowButton.BackButton
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.core.presentation_layer.navigation.Screen
import com.project.toko.core.presentation_layer.navigation.SetupNavGraph
import com.project.toko.core.presentation_layer.theme.LightBottomBarColor
import com.project.toko.producerDetailedScreen.viewModel.ProducerFullViewModel
import com.project.toko.staffDetailedScreen.viewModel.PersonByIdViewModel

@Composable
fun TokoAppActivator(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
) {

    val currentDetailScreenId = viewModelProvider[DetailScreenViewModel::class.java].loadedId
    val characterDetailScreenId = viewModelProvider[CharacterFullByIdViewModel::class.java].loadedId
    val personDetailScreenId = viewModelProvider[PersonByIdViewModel::class.java].loadedId
    val producerDetailScreenId = viewModelProvider[ProducerFullViewModel::class.java].loadedId
    var showBackArrow by remember { mutableStateOf(false) }
    var lastSelectedScreen by rememberSaveable { mutableStateOf<String?>(null) }

    navController.addOnDestinationChangedListener { _, destination, arguments ->
        if (destination.route == Screen.Detail.route) {
            currentDetailScreenId.intValue = arguments?.getInt("id") ?: 0
        }

        showBackArrow = when (destination.route) {
            Screen.Home.route -> false
            Screen.RandomAnimeOrManga.route -> false
            Screen.Favorites.route -> false
            Screen.DetailOnWholeCast.route -> {
                lastSelectedScreen = destination.route
                false
            }

            Screen.DetailOnWholeStaff.route -> {
                lastSelectedScreen = destination.route
                false
            }

            Screen.Detail.route -> {
                lastSelectedScreen = destination.route
                false
            }

            Screen.Nothing.route -> {
                lastSelectedScreen = destination.route
                false
            }

//            Screen.DetailOnStaff.route -> {
//                lastSelectedScreen = destination.route
//                false
//            }
//
//            Screen.DetailOnCast.route -> {
//                lastSelectedScreen = destination.route
//                false
//            }

            else -> {
                lastSelectedScreen = destination.route
                true
            }
        }
    }

    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = modifier
//                .requiredHeight()
            ) {
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Drawer Item") },
                    selected = false,
                    onClick = {
                    }
                )
                // ...other drawer items
            }
        }

    ) {

        Scaffold(bottomBar = {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth(1f)
//                    .fillMaxHeight(1f)
////                    .padding(bottom = 45.dp)
//                ,
//                verticalArrangement = Arrangement.Bottom,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {

//                Row {
//                    MyFloatingButton(
//                        showButton = showButton,
//                        viewModelProvider = viewModelProvider,
//                        modifier = modifier
//                    )
//                }

//                Row {


            SecondBottomNavigationBar(
                navController = navController,
                currentDetailScreenId = currentDetailScreenId,
                characterDetailScreenId = characterDetailScreenId,
                personDetailScreenId = personDetailScreenId,
                producerDetailScreenId = producerDetailScreenId,
                modifier = modifier,
                lastSelectedScreen = lastSelectedScreen,
                imageLoader = svgImageLoader
            )

//                    BottomNavigationBar(
//                        navController = navController,
//                        currentDetailScreenId = currentDetailScreenId,
//                        characterDetailScreenId = characterDetailScreenId,
//                        personDetailScreenId = personDetailScreenId,
//                        producerDetailScreenId = producerDetailScreenId,
//                        modifier = modifier,
//                        lastSelectedScreen = lastSelectedScreen
//                    )

//                }


//            }
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
//                    exoPlayer = exoPlayer, playerView = playerView
                )
            },
            topBar = {
                if (showBackArrow)
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = modifier.size(60.dp)
                    ) {
                        BackButton(modifier = modifier, onClick = {
                            navController.popBackStack()
                        })
                    }
            }
        )
    }

}

@Composable
fun SecondBottomNavigationBar(
    navController: NavController,
    currentDetailScreenId: MutableState<Int>,
    modifier: Modifier,
    producerDetailScreenId: MutableState<Int>,
    characterDetailScreenId: MutableState<Int>,
    personDetailScreenId: MutableState<Int>,
    lastSelectedScreen: String?,
    imageLoader: ImageLoader

) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val detailScreenButtonIsSelected = when (currentRoute) {
        Screen.Home.route -> false
        Screen.Favorites.route -> false
        Screen.RandomAnimeOrManga.route -> false
        else -> true
    }

    BottomAppBar(
        modifier = modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .blur(200.dp)
            .height(50.dp),
        containerColor = LightBottomBarColor.copy(0.6f)
    ) {
        NavigationBarItem(selected = false, onClick = {
            try {
                navController.navigate(Screen.Home.route) {

                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    navController.graph.startDestinationRoute?.let { _ ->
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                    }

                }
            } catch (e: IllegalArgumentException) {

                Log.e("CATCH", Screen.Home.route + " " + e.message.toString())

            }


        }, icon = {

            if (currentRoute != Screen.Home.route) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.home, imageLoader = imageLoader
                    ), contentDescription = null, modifier = modifier.size(35.dp)
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.homefilled, imageLoader = imageLoader
                    ), contentDescription = null, modifier = modifier.size(35.dp)
                )
            }
        })
        NavigationBarItem(selected = false, onClick = {
            try {
                when (lastSelectedScreen) {
                    Screen.Detail.route -> {
                        navController.navigate("detail_screen/${currentDetailScreenId.value}") {
                            navController.graph.startDestinationRoute?.let { _ ->
                                launchSingleTop = true
                            }
                        }

                        Log.d("last stack membor", "detail_screen/${currentDetailScreenId.value}")
                    }

                    Screen.CharacterDetail.route -> {
                        navController.navigate("detail_on_character/${characterDetailScreenId.value}") {
                            navController.graph.startDestinationRoute?.let { _ ->
                                launchSingleTop = true
                            }
                        }
                        Log.d(
                            "last stack membor",
                            "detail_on_character/${characterDetailScreenId.value}"
                        )
                    }

                    Screen.StaffDetail.route -> {
                        navController.navigate("detail_on_staff/${personDetailScreenId.value}") {
                            navController.graph.startDestinationRoute?.let { _ ->
                                launchSingleTop = true
                            }
                        }
                        Log.d("last stack membor", "detail_on_staff/${personDetailScreenId.value}")
                    }

                    Screen.ProducerDetail.route -> {
                        navController.navigate("detail_on_producer/${producerDetailScreenId.value}/full") {
                            navController.graph.startDestinationRoute?.let { _ ->
                                launchSingleTop = true
                            }
                        }
                        Log.d(
                            "last stack membor",
                            "detail_on_producer/${producerDetailScreenId.value}/full"
                        )
                    }

                    Screen.DetailOnWholeStaff.route -> {
                        navController.navigate(Screen.DetailOnWholeStaff.route) {
                            navController.graph.startDestinationRoute?.let { _ ->
                                launchSingleTop = true
                            }
                        }
                        Log.d("last stack membor", Screen.DetailOnWholeStaff.route)
                    }

                    Screen.DetailOnWholeCast.route -> {
                        navController.navigate(Screen.DetailOnWholeCast.route) {
                            navController.graph.startDestinationRoute?.let { _ ->
                                launchSingleTop = true
                            }
                        }
                        Log.d("last stack membor", Screen.DetailOnWholeCast.route)
                    }

                    else -> {
                        navController.navigate(Screen.Nothing.route) {
                            launchSingleTop = true
                            Log.d("last stack membor", Screen.Nothing.route)
                        }
                    }
                }
            } catch (e: IllegalArgumentException) {
                Log.e("CATCH", Screen.Detail.route + " " + e.message.toString())
            }
        }, icon = {

            if (detailScreenButtonIsSelected) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.detailfilled, imageLoader = imageLoader
                    ), contentDescription = null, modifier = modifier.size(30.dp)
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.detail, imageLoader = imageLoader
                    ), contentDescription = null, modifier = modifier.size(30.dp)
                )
            }
        })
        NavigationBarItem(selected = false, onClick = {
            try {
                navController.navigate(Screen.Favorites.route) {

                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    navController.graph.startDestinationRoute?.let { _ ->
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                    }

                }
            } catch (e: IllegalArgumentException) {

                Log.e("CATCH", Screen.Favorites.route + " " + e.message.toString())

            }
        }, icon = {
            if (currentRoute != Screen.Favorites.route) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.bookmarkempty, imageLoader = imageLoader
                    ), contentDescription = null, modifier = modifier.size(30.dp)
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.bookmarkfilled, imageLoader = imageLoader
                    ), contentDescription = null, modifier = modifier.size(30.dp)
                )
            }
        })
        NavigationBarItem(selected = false, onClick = {
            try {
                navController.navigate(Screen.RandomAnimeOrManga.route) {

                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    navController.graph.startDestinationRoute?.let { _ ->
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                    }

                }
            } catch (e: IllegalArgumentException) {

                Log.e(
                    "CATCH",
                    Screen.RandomAnimeOrManga.route + " " + e.message.toString()
                )

            }
        }, icon = {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.shuffle, imageLoader = imageLoader
                ), contentDescription = null, modifier = modifier.size(30.dp)
            )

        })
    }
}

//@Composable
//fun BottomNavigationBar(
//    navController: NavController,
//    currentDetailScreenId: MutableState<Int>,
//    modifier: Modifier,
//    producerDetailScreenId: MutableState<Int>,
//    characterDetailScreenId: MutableState<Int>,
//    personDetailScreenId: MutableState<Int>,
//    lastSelectedScreen: String?
//
//) {
//
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//
//    Row(
//        modifier = modifier
//            .clip(
//                RoundedCornerShape(10.dp)
//            )
//            .fillMaxWidth(1f)
//            .background(LightBottomBarColor)
//            .height(50.dp),
//
//        horizontalArrangement = Arrangement.SpaceAround,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        if (currentRoute != null) {
//            Log.d("currentRoute", currentRoute)
//
//        }
//
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = modifier
//                .width(80.dp)
//                .clickable {
//                    try {
//                        navController.navigate(Screen.Home.route) {
//
//                            // Avoid multiple copies of the same destination when
//                            // reselecting the same item
//                            navController.graph.startDestinationRoute?.let { _ ->
//                                launchSingleTop = true
//                                // Restore state when reselecting a previously selected item
//                            }
//
//                        }
//                    } catch (e: IllegalArgumentException) {
//
//                        Log.e("CATCH", Screen.Home.route + " " + e.message.toString())
//
//                    }
//                }
//                .padding(horizontal = 10.dp, vertical = 10.dp)
//
//        ) {
//            Icon(
//                imageVector = ImageVector.vectorResource(id = Screen.Home.iconId!!),
//                contentDescription = Screen.Home.contentDescription,
//                modifier = modifier
//                    .size(30.dp),
//                tint = LightIconTint
//            )
//        }
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = modifier
//                .width(80.dp)
//                .clickable {
//                    try {
//                        when (lastSelectedScreen) {
//                            Screen.Detail.route -> {
//                                navController.navigate("detail_screen/${currentDetailScreenId.value}") {
//                                    navController.graph.startDestinationRoute?.let { _ ->
//                                        launchSingleTop = true
//                                    }
//                                }
//                            }
//
//                            Screen.CharacterDetail.route -> {
//                                navController.navigate("detail_on_character/${characterDetailScreenId.value}") {
//                                    navController.graph.startDestinationRoute?.let { _ ->
//                                        launchSingleTop = true
//                                    }
//                                }
//                            }
//
//                            Screen.StaffDetail.route -> {
//                                navController.navigate("detail_on_staff/${personDetailScreenId.value}") {
//                                    navController.graph.startDestinationRoute?.let { _ ->
//                                        launchSingleTop = true
//                                    }
//                                }
//                            }
//
//                            Screen.ProducerDetail.route -> {
//                                navController.navigate("detail_on_producer/${producerDetailScreenId.value}/full") {
//                                    navController.graph.startDestinationRoute?.let { _ ->
//                                        launchSingleTop = true
//                                    }
//                                }
//                            }
//
//                            else -> {
//                                navController.navigate(Screen.Nothing.route) {
//                                    launchSingleTop = true
//                                }
//                            }
//                        }
//                    } catch (e: IllegalArgumentException) {
//                        Log.e("CATCH", Screen.Detail.route + " " + e.message.toString())
//                    }
//                }
//                .padding(horizontal = 10.dp, vertical = 10.dp)
//        ) {
//            Icon(
//                imageVector = ImageVector.vectorResource(id = Screen.Detail.iconId!!),
//                contentDescription = Screen.Detail.contentDescription,
//                modifier = modifier
//                    .size(30.dp),
//                tint = LightIconTint
//
//            )
//        }
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = modifier
//                .width(80.dp)
//                .clickable {
//                    try {
//                        navController.navigate(Screen.Favorites.route) {
//
//                            // Avoid multiple copies of the same destination when
//                            // reselecting the same item
//                            navController.graph.startDestinationRoute?.let { _ ->
//                                launchSingleTop = true
//                                // Restore state when reselecting a previously selected item
//                            }
//
//                        }
//                    } catch (e: IllegalArgumentException) {
//
//                        Log.e("CATCH", Screen.Favorites.route + " " + e.message.toString())
//
//                    }
//                }
//                .padding(horizontal = 10.dp, vertical = 10.dp)
//        ) {
//            Icon(
//                imageVector = ImageVector.vectorResource(id = Screen.Favorites.iconId!!),
//                contentDescription = Screen.Favorites.contentDescription,
//                modifier = modifier
//                    .size(30.dp),
//                tint = LightIconTint
//
//
//            )
//        }
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = modifier
//                .width(80.dp)
//                .clickable {
//                    try {
//                        navController.navigate(Screen.RandomAnimeOrManga.route) {
//
//                            // Avoid multiple copies of the same destination when
//                            // reselecting the same item
//                            navController.graph.startDestinationRoute?.let { _ ->
//                                launchSingleTop = true
//                                // Restore state when reselecting a previously selected item
//                            }
//
//                        }
//                    } catch (e: IllegalArgumentException) {
//
//                        Log.e(
//                            "CATCH",
//                            Screen.RandomAnimeOrManga.route + " " + e.message.toString()
//                        )
//
//                    }
//                }
//                .padding(horizontal = 10.dp, vertical = 10.dp)
//        ) {
//            Icon(
//                imageVector = ImageVector.vectorResource(id = Screen.RandomAnimeOrManga.iconId!!),
//                contentDescription = Screen.RandomAnimeOrManga.contentDescription,
//                modifier = modifier
//                    .size(30.dp),
//
//                tint = LightIconTint
//
//            )
//        }
//    }
//
//}
//


//@Composable
//fun MyFloatingButton(
//    showButton: Boolean,
//    viewModelProvider: ViewModelProvider,
////    context: Context,
//    modifier: Modifier
//) {
//
//
//    val items = mutableListOf("Planned", "Watching", "Watched", "Dropped")
//    val detailScreenViewModel = viewModelProvider[DetailScreenViewModel::class.java]
//    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
//    val detailScreenState by viewModelProvider[DetailScreenViewModel::class.java]
//        .animeDetails.collectAsStateWithLifecycle()
//
//    LaunchedEffect(key1 = null) {
//        val flow = daoViewModel.containsInDataBase(detailScreenState?.mal_id ?: 0)
//
//        if (flow.first()) {
//            items.add(4, "Delete")
//        }
//    }
//
//
//    var expanded by remember { mutableStateOf(false) }
//    var selectedItem by remember { mutableStateOf("") }
//
//    AnimatedVisibility(
//        visible = showButton,
//        enter = slideInVertically(
//            initialOffsetY = { -it },
//            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
//        ) + fadeIn(animationSpec = tween(durationMillis = 500)),
//        exit = slideOutVertically(
//            targetOffsetY = { -it },
//            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
//        ) + fadeOut(animationSpec = tween(durationMillis = 500))
//    ) {
//
//        Box(
//            modifier = modifier
//                .height(70.dp)
//                .width(70.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            FloatingActionButton(
//                onClick = {
//                    detailScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
//                        expanded = !expanded
//                    }
//
//                },
//                containerColor = LightGreen.copy(alpha = 0.6f),
//                modifier = modifier
//                    .height(50.dp)
//                    .width(50.dp)
//            ) {
//                if (expanded) {
//                    Icon(
//                        modifier = Modifier.size(30.dp),
//                        imageVector = Icons.Filled.Close,
//                        contentDescription = "Localized description"
//                    )
//                } else {
//                    Icon(
//                        modifier = Modifier.size(30.dp),
//                        imageVector = Icons.Filled.Add,
//                        contentDescription = "Localized description"
//                    )
//                }
//            }
//        }
//    }
//    DropdownMenu(
//        expanded = expanded,
//        onDismissRequest = { expanded = false },
//        modifier = modifier
//            .background(LightGreen),
//        offset = DpOffset(x = (20).dp, y = (-250).dp)
//    ) {
//        items.forEach { item ->
//            DropdownMenuItem(
//                onClick = {
//                    detailScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
//                        selectedItem = item
//                        if (selectedItem == "Delete") {
//                            detailScreenState?.let { data ->
//                                daoViewModel.removeFromDataBase(data.mal_id)
//                            }
//                        } else {
//                            detailScreenState?.let { data ->
//                                daoViewModel.addToCategory(
//                                    AnimeItem(
//                                        data.mal_id,
//                                        anime = data.title,
//                                        score = formatScore(data.score),
//                                        scored_by = formatScoredBy(data.scored_by),
//                                        animeImage = data.images.jpg.large_image_url,
//                                        category = selectedItem
//                                    )
//                                )
//                            }
//                        }
//                        // on touched - dropDownMenu cancels
//                        expanded = false
//                    }
//                },
//                text = {
//                    Text(text = item)
//                })
//        }
//    }
//
//}