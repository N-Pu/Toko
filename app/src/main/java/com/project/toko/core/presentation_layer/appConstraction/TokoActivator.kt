package com.project.toko.core.presentation_layer.appConstraction

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.project.toko.homeScreen.viewModel.HomeScreenViewModel
import com.project.toko.producerDetailedScreen.viewModel.ProducerFullViewModel
import com.project.toko.personDetailedScreen.viewModel.PersonByIdViewModel

@Composable
fun TokoAppActivator(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
) {
    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()

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

            else -> {
                lastSelectedScreen = destination.route
                true
            }
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            ShowDrawerContent(
                modifier = modifier,
                imageLoader = svgImageLoader,
                viewModelProvider = viewModelProvider
            )
        }
    ) {

        Scaffold(bottomBar = {

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
private fun SecondBottomNavigationBar(
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


@Composable
private fun ShowDrawerContent(
    modifier: Modifier,
    imageLoader: ImageLoader,
    viewModelProvider: ViewModelProvider
) {
    val homeScreenViewModel = viewModelProvider[HomeScreenViewModel::class.java]
//    ModalDrawerSheet(
//        modifier = modifier.background(androidx.compose.ui.graphics.Color.Transparent)
////                .requiredHeight()
//    ) {
    Column {
        Row {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.Transparent)
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topEnd = 20.dp))
                .background(Color.White)
        ) {
//            Text(
//                "Toko", fontWeight = FontWeight.ExtraBold,
//                fontSize = 22.sp, modifier = Modifier.padding(16.dp)
//            )
            Divider(thickness = 3.dp)
            NavigationDrawerItem(
                label = {
                    Text(
                        text = "NSFW",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = modifier.padding(start = 20.dp)
                    )
                },
                selected = false,
                onClick = {
                },
                badge = {
                    Switch(checked = homeScreenViewModel.safeForWork.value, onCheckedChange = {
                        homeScreenViewModel.safeForWork.value = it
                    }, colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(65, 65, 65),
                        checkedTrackColor = Color(251, 251, 251),
                        checkedBorderColor = Color(65, 65, 65),
                        uncheckedThumbColor = Color(65, 65, 65),
                        uncheckedTrackColor = Color(251, 251, 251),
                        uncheckedBorderColor = Color(65, 65, 65),

                        ),
                        thumbContent = if (homeScreenViewModel.safeForWork.value) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                    tint = Color(251, 251, 251)
                                )
                            }
                        } else {
                            null
                        })
                },
            )
            Divider(thickness = 3.dp)
            NavigationDrawerItem(
                modifier = modifier.background(Color(104, 190, 174).copy(alpha = 0.24f)),
                colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
                label = {
                    Text(
                        text = "Help/FAQ",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = modifier.padding(start = 20.dp)
                    )
                },
                selected = false,
                onClick = {
                },
                badge = {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.arrowright, imageLoader = imageLoader
                        ), contentDescription = null, modifier = modifier.size(17.dp)
                    )
                },
            )
            Divider(thickness = 3.dp)
            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Contact Support",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = modifier.padding(start = 20.dp)
                    )
                },
                selected = false,
                onClick = {
                },
                badge = {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.openbrowser, imageLoader = imageLoader
                        ), contentDescription = null, modifier = modifier.size(30.dp)
                    )
                },
            )
            Divider(thickness = 3.dp)
            NavigationDrawerItem(
                modifier = modifier.background(Color(104, 190, 174).copy(alpha = 0.24f)),
                colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
                label = {
                    Text(
                        text = "Legal",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = modifier.padding(start = 20.dp)
                    )
                },
                selected = false,
                onClick = {
                },
                badge = {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.arrowright, imageLoader = imageLoader
                        ), contentDescription = null, modifier = modifier.size(17.dp)
                    )
                },
            )
            Divider(thickness = 3.dp)
            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Export Data",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = modifier.padding(start = 20.dp)
                    )
                },
                selected = false,
                onClick = {
                },
                badge = {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.export, imageLoader = imageLoader
                        ), contentDescription = null, modifier = modifier.size(30.dp)
                    )
                },
            )
            Divider(thickness = 3.dp)
        }
//    }
    }
}