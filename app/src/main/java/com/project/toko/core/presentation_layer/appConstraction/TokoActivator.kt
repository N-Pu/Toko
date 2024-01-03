package com.project.toko.core.presentation_layer.appConstraction

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.project.toko.R
import com.project.toko.characterDetailedScreen.viewModel.CharacterFullByIdViewModel
import com.project.toko.core.presentation_layer.navigation.Screen
import com.project.toko.core.presentation_layer.navigation.SetupNavGraph
import com.project.toko.core.presentation_layer.theme.evolventaBoldFamily
import com.project.toko.core.utils.share.openSite
import com.project.toko.daoScreen.daoViewModel.DaoViewModel
import com.project.toko.daoScreen.model.AnimeStatus
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.homeScreen.viewModel.HomeScreenViewModel
import com.project.toko.personDetailedScreen.viewModel.PersonByIdViewModel
import kotlinx.coroutines.launch


@Composable
fun AppActivator(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    componentActivity: ComponentActivity,
    onThemeChange: () -> Unit,
    isInDarkTheme: () -> Boolean,
    svgImageLoader: ImageLoader
) {

    var currentDetailScreenId by viewModelProvider[DetailScreenViewModel::class.java].loadedId
    var lastSelectedScreen by remember { mutableStateOf<String?>(null) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    LaunchedEffect(lastSelectedScreen) {
        navController.addOnDestinationChangedListener { _, destination, arguments ->

            when (destination.route) {

                Screen.Detail.route -> {
                    currentDetailScreenId = arguments?.getInt("id") ?: 0
                    lastSelectedScreen = destination.route
                }

                Screen.Home.route,
                Screen.RandomAnimeOrManga.route,
                Screen.Favorites.route -> {
                }

                else -> {
                    lastSelectedScreen = destination.route
                }
            }
        }
    }


    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {
            ShowDrawerContent(
                modifier = modifier,
                imageLoader = svgImageLoader,
                viewModelProvider = viewModelProvider,
                componentActivity = componentActivity,
                onThemeChange = onThemeChange,
                darkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader
            )
        }
    ) {
        Scaffold(bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentDetailScreenId = { currentDetailScreenId },
                modifier = modifier,
                lastSelectedScreen = lastSelectedScreen,
                imageLoader = svgImageLoader,
                viewModelProvider = viewModelProvider
            )
        },
            content = { padding ->
                padding.calculateTopPadding()
                SetupNavGraph(
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    modifier = modifier,
                    isInDarkTheme = isInDarkTheme,
                    drawerState = drawerState,
                    svgImageLoader = svgImageLoader
                )
            }
        )
    }
}


@Composable
private fun BottomNavigationBar(
    navController: NavController,
    currentDetailScreenId: () -> Int,
    modifier: Modifier,
    lastSelectedScreen: String?,
    imageLoader: ImageLoader,
    viewModelProvider: ViewModelProvider
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var detailScreenButtonIsSelected = false
    val characterDetailScreenId by viewModelProvider[CharacterFullByIdViewModel::class.java].loadedId
    val personDetailScreenId by viewModelProvider[PersonByIdViewModel::class.java].loadedId

    LaunchedEffect(key1 = currentRoute) {
        detailScreenButtonIsSelected = when (currentRoute) {
            Screen.Home.route, Screen.Favorites.route, Screen.RandomAnimeOrManga.route -> {
                false
            }

            else -> {
                true
            }
        }
    }

    Row(
        modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)
            )
            .height(50.dp)
            .background(MaterialTheme.colorScheme.onBackground)
        ,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable {
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
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            if (currentRoute != Screen.Home.route) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.home, imageLoader = imageLoader
                    ), contentDescription = null, modifier = modifier.size(35.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.homefilled, imageLoader = imageLoader
                    ), contentDescription = null, modifier = modifier.size(35.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)

                )
            }
        }
        Column(
            modifier = modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable {
                    try {
                        when (lastSelectedScreen) {
                            Screen.Detail.route -> {
                                navController.navigate("detail_screen/${currentDetailScreenId()}") {
                                    navController.graph.startDestinationRoute?.let { _ ->
                                        launchSingleTop = true
                                    }
                                }

                                Log.d(
                                    "last stack membor",
                                    "detail_screen/${currentDetailScreenId()}"
                                )
                            }

                            Screen.CharacterDetail.route -> {
                                navController.navigate("detail_on_character/${characterDetailScreenId}") {
                                    navController.graph.startDestinationRoute?.let { _ ->
                                        launchSingleTop = true
                                    }
                                }
                                Log.d(
                                    "last stack membor",
                                    "detail_on_character/${characterDetailScreenId}"
                                )
                            }

                            Screen.StaffDetail.route -> {
                                navController.navigate("detail_on_staff/${personDetailScreenId}") {
                                    navController.graph.startDestinationRoute?.let { _ ->
                                        launchSingleTop = true
                                    }
                                }
                                Log.d(
                                    "last stack membor",
                                    "detail_on_staff/${personDetailScreenId}"
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
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            if (detailScreenButtonIsSelected) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.detailfilled, imageLoader = imageLoader
                    ), contentDescription = null, modifier = modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.detail, imageLoader = imageLoader
                    ), contentDescription = null, modifier = modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                )
            }
        }
        Column(
            modifier = modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable {
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
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            if (currentRoute != Screen.Favorites.route) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.bookmarkempty, imageLoader = imageLoader
                    ), contentDescription = null, modifier = modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.bookmarkfilled, imageLoader = imageLoader
                    ), contentDescription = null, modifier = modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                )
            }
        }
        Column(
            modifier = modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable {
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
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.shuffle, imageLoader = imageLoader
                ), contentDescription = null, modifier = modifier.size(30.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
            )
        }

    }
}


@Composable
private fun ShowDrawerContent(
    modifier: Modifier,
    imageLoader: ImageLoader,
    viewModelProvider: ViewModelProvider,
    componentActivity: ComponentActivity,
    onThemeChange: () -> Unit,
    darkTheme: () -> Boolean,
    svgImageLoader: ImageLoader
) {
    val homeScreenViewModel = viewModelProvider[HomeScreenViewModel::class.java]
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    var isHelpFAQOpen by remember { mutableStateOf(false) }
    var isLegalOpen by remember { mutableStateOf(false) }
    var isDeleteDaoOpen by remember { mutableStateOf(false) }
    val isExportDataPopUpDialogOpen = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val customModifier =
        modifier
            .fillMaxWidth(0.8f)
            .height(70.dp)
            .clip(CardDefaults.shape)
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
            .clickable {
                daoViewModel.exportDB("Main.db", "com.project.toko")
            }


    Column {
        Row {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.Transparent)
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(topEnd = 20.dp))
                .background(MaterialTheme.colorScheme.surfaceTint)
                .verticalScroll(rememberScrollState())
        ) {
            Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.onSurface)
            NavigationDrawerItem(
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.surfaceTint,
                    unselectedContainerColor = MaterialTheme.colorScheme.surfaceTint
                ),
                label = {
                    Text(
                        text = "NSFW",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = modifier.padding(start = 20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = evolventaBoldFamily
                    )
                },
                selected = false,
                onClick = {
                },
                badge = {
                    Switch(checked = homeScreenViewModel.isNSFWActive.value, onCheckedChange = {
                        homeScreenViewModel.saveNSFWData(it)
                        homeScreenViewModel.isNSFWActive.value = it
                    }, colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.inversePrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.surfaceTint,
                        checkedBorderColor = MaterialTheme.colorScheme.inversePrimary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.inversePrimary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceTint,
                        uncheckedBorderColor = MaterialTheme.colorScheme.inversePrimary,
                    ),
                        thumbContent = if (homeScreenViewModel.isNSFWActive.value) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        } else {
                            null
                        })
                },
            )
            Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.onSurface)
            NavigationDrawerItem(
                modifier = modifier.background(MaterialTheme.colorScheme.inverseSurface),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color.Transparent,
                    unselectedContainerColor = Color.Transparent
                ),
                label = {
                    Text(
                        text = "Help/FAQ",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = modifier.padding(start = 20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = evolventaBoldFamily
                    )
                },
                selected = false,
                onClick = {
                    isHelpFAQOpen = !isHelpFAQOpen
                },
                badge = {
                    if (isHelpFAQOpen) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.arrowdown, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(17.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.arrowright, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(17.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                },
            )
            Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.onSurface)
            if (isHelpFAQOpen) {
                NavigationDrawerItem(
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.surfaceTint,
                        unselectedContainerColor = MaterialTheme.colorScheme.surfaceTint
                    ),
                    label = {
                        Text(
                            text = "Future of the app",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            modifier = modifier.padding(start = 20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = evolventaBoldFamily
                        )
                    },
                    selected = false,
                    onClick = {
                        context.openSite("https://sites.google.com/view/toko-your-own-anime-library/future?authuser=0")
                    },
                    badge = {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.openbrowser, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(30.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    },
                )

                Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.onSurface)
                NavigationDrawerItem(
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.surfaceTint,
                        unselectedContainerColor = MaterialTheme.colorScheme.surfaceTint
                    ),
                    label = {
                        Text(
                            text = "Bugs",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            modifier = modifier.padding(start = 20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = evolventaBoldFamily
                        )
                    },
                    selected = false,
                    onClick = {
                        context.openSite("https://sites.google.com/view/toko-your-own-anime-library/bugs?authuser=0")
                    },
                    badge = {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.openbrowser, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(30.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    },
                )
                Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.onSurface)
            }
            NavigationDrawerItem(
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.surfaceTint,
                    unselectedContainerColor = MaterialTheme.colorScheme.surfaceTint
                ),
                label = {
                    Text(
                        text = "Contact Support",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = modifier.padding(start = 20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = evolventaBoldFamily
                    )
                },
                selected = false,
                onClick = {
                    context.openSite("https://discord.gg/arJvEJ6RJb")
                },
                badge = {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.openbrowser, imageLoader = imageLoader
                        ), contentDescription = null, modifier = modifier.size(30.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                    )
                },
            )
            Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.onSurface)
            NavigationDrawerItem(
                modifier = modifier.background(MaterialTheme.colorScheme.inverseSurface),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color.Transparent,
                    unselectedContainerColor = Color.Transparent
                ),
                label = {
                    Text(
                        text = "Legal",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = modifier.padding(start = 20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = evolventaBoldFamily
                    )
                },
                selected = false,
                onClick = {
                    isLegalOpen = !isLegalOpen
                },
                badge = {
                    if (isLegalOpen) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.arrowdown, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(17.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.arrowright, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(17.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                },
            )
            Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.onSurface)
            if (isLegalOpen) {
                NavigationDrawerItem(
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.surfaceTint,
                        unselectedContainerColor = MaterialTheme.colorScheme.surfaceTint
                    ),
                    label = {
                        Text(
                            text = "Resource",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            modifier = modifier.padding(start = 20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = evolventaBoldFamily
                        )
                    },
                    selected = false,
                    onClick = {
                        context.openSite("https://sites.google.com/view/toko-your-own-anime-library/resource?authuser=0")
                    },
                    badge = {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.openbrowser, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(30.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    },
                )
                Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.onSurface)
            }

            NavigationDrawerItem(
                modifier = modifier.background(MaterialTheme.colorScheme.inverseSurface),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color.Transparent,
                    unselectedContainerColor = Color.Transparent
                ),
                label = {
                    Text(
                        text = "Data",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = modifier.padding(start = 20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = evolventaBoldFamily
                    )
                },
                selected = false,
                onClick = {
                    isDeleteDaoOpen = !isDeleteDaoOpen
                },
                badge = {
                    if (isDeleteDaoOpen) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.arrowdown, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(17.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.arrowright, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(17.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                },
            )
            Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.onSurface)
            if (isDeleteDaoOpen) {
                AnimeListTypesToDelete(daoViewModel = daoViewModel, modifier = modifier)
            }



            NavigationDrawerItem(
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.surfaceTint,
                    unselectedContainerColor = MaterialTheme.colorScheme.surfaceTint
                ),
                label = {
                    Text(
                        text = "Export Data",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = modifier.padding(start = 20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = evolventaBoldFamily
                    )
                },
                selected = false,
                onClick = {
                    onClickRequestPermission(componentActivity, isExportDataPopUpDialogOpen)
                },
                badge = {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.export, imageLoader = imageLoader
                        ), contentDescription = null, modifier = modifier.size(30.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                    )
                },
            )
            Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.onSurface)

        }
        Column(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth(0.8f)
                .background(MaterialTheme.colorScheme.surfaceTint),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = if (darkTheme()) R.drawable.sun else R.drawable.moon,
                    imageLoader = svgImageLoader
                ),
                contentDescription = null,
                modifier = modifier
                    .size(50.dp)
                    .padding(bottom = 10.dp, end = 5.dp)
                    .clickable {
                        onThemeChange()
                    }
            )
        }
    }


    if (isExportDataPopUpDialogOpen.value) {
        Dialog(
            onDismissRequest = {
                isExportDataPopUpDialogOpen.value = false
            }, properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            )
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f), contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceTint)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = modifier.fillMaxSize()
                    ) {
                        Row(
                            modifier = modifier
                                .fillMaxHeight(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Export Data?",
                                fontSize = 35.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = evolventaBoldFamily
                            )
                        }
                        Row(
                            modifier = customModifier,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Save Data",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontFamily = evolventaBoldFamily
                            )
                        }
                        Spacer(modifier = modifier.height(10.dp))
                        Row(
                            modifier = modifier
                                .fillMaxWidth(0.8f)
                                .height(70.dp)
                                .clip(CardDefaults.shape)
                                .border(
                                    4.dp,
                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                    CardDefaults.shape
                                )
                                .clickable {
                                    Toast
                                        .makeText(
                                            context,
                                            "Will be added in next update!",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Upload Data",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = evolventaBoldFamily
                            )
                        }
                        Spacer(modifier = modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

// Storage Permissions
private const val REQUEST_EXTERNAL_STORAGE = 1
private val PERMISSIONS_STORAGE = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

private fun onClickRequestPermission(
    componentActivity: ComponentActivity,
    isExportDataPopUpDialogOpen: MutableState<Boolean>
) {

    when {
        ContextCompat.checkSelfPermission(
            componentActivity.applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED -> {
            isExportDataPopUpDialogOpen.value = true
        }

        ActivityCompat.shouldShowRequestPermissionRationale(
            componentActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) -> {
            ActivityCompat.requestPermissions(
                componentActivity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }

        else -> {
            Toast
                .makeText(
                    componentActivity,
                    "Permission Denied!",
                    Toast.LENGTH_SHORT
                )
                .show()
            ActivityCompat.requestPermissions(
                componentActivity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }
}

@Composable
fun AnimeListTypesToDelete(
    daoViewModel: DaoViewModel,
    modifier: Modifier
) {
    val animeListTypes = AnimeStatus.values()
    val isDeleteDataOpen = remember { mutableStateOf(false) }
    val currentSelectedAnimeListType = daoViewModel.currentSelectedAnimeListType
    val customModifier =
        modifier
            .fillMaxWidth(0.8f)
            .height(70.dp)
            .clip(CardDefaults.shape)
            .background(MaterialTheme.colorScheme.onPrimaryContainer)


    animeListTypes.forEach { type ->
        NavigationDrawerItem(
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = MaterialTheme.colorScheme.surfaceTint,
                unselectedContainerColor = MaterialTheme.colorScheme.surfaceTint
            ),
            label = {
                Text(
                    text = "Delete " + type.route,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = evolventaBoldFamily
                )
            },
            selected = false,
            onClick = {
                currentSelectedAnimeListType.value = type.route
                isDeleteDataOpen.value = true
            },
            badge = {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete ${type.route}",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            },
        )
        Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.onSurface)


        if (isDeleteDataOpen.value) {
            DeleteDialog(
                modifier,
                customModifier,
                isDeleteDataOpen,
                currentSelectedAnimeListType.value,
                deleteProcess = {
                    when (currentSelectedAnimeListType.value) {
                        AnimeStatus.FAVORITE.route -> {
                            daoViewModel.viewModelScope.launch {
                                daoViewModel.deleteAllFavorite()
                            }
                        }

                        AnimeStatus.PERSON.route -> {
                            daoViewModel.viewModelScope.launch {
                                daoViewModel.deleteAllPeople()
                            }
                        }

                        AnimeStatus.CHARACTER.route -> {
                            daoViewModel.viewModelScope.launch {
                                daoViewModel.deleteAllCharacters()
                            }
                        }

                        else -> {
                            daoViewModel.viewModelScope.launch {
                                daoViewModel.deleteAnimeByCategory(currentSelectedAnimeListType.value)
                            }
                        }
                    }

                })
        }
    }
}


@Composable
fun DeleteDialog(
    modifier: Modifier,
    customModifier: Modifier,
    isExportDataPopUpDialogOpen: MutableState<Boolean>,
    animeListType: String,
    deleteProcess: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            isExportDataPopUpDialogOpen.value = false
        }, properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f), contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceTint)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxHeight(0.4f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Delete $animeListType data?",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = evolventaBoldFamily
                        )
                    }
                    Row(
                        modifier = customModifier.clickable {
                            deleteProcess()
                            isExportDataPopUpDialogOpen.value = false
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Yes",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontFamily = evolventaBoldFamily
                        )
                    }
                    Spacer(modifier = modifier.height(10.dp))
                    Row(
                        modifier = modifier
                            .fillMaxWidth(0.8f)
                            .height(70.dp)
                            .clip(CardDefaults.shape)
                            .border(
                                4.dp,
                                MaterialTheme.colorScheme.onPrimaryContainer,
                                CardDefaults.shape
                            )
                            .clickable {
                                isExportDataPopUpDialogOpen.value = false
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = evolventaBoldFamily
                        )
                    }
                    Spacer(modifier = modifier.height(20.dp))
                }
            }
        }
    }
}
