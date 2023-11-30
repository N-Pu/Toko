package com.project.toko.core.presentation_layer.appConstraction

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import coil.decode.SvgDecoder
import com.project.toko.R
import com.project.toko.characterDetailedScreen.viewModel.CharacterFullByIdViewModel
import com.project.toko.core.dao.MainDb
import com.project.toko.core.presentation_layer.navigation.Screen
import com.project.toko.core.presentation_layer.navigation.SetupNavGraph
import com.project.toko.core.presentation_layer.theme.LightBottomBarColor
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.homeScreen.viewModel.HomeScreenViewModel
import com.project.toko.personDetailedScreen.viewModel.PersonByIdViewModel
import com.project.toko.producerDetailedScreen.viewModel.ProducerFullViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Composable
fun TokoAppActivator(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    componentActivity: ComponentActivity,
    mainDb: MainDb
) {
    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()

    val currentDetailScreenId = viewModelProvider[DetailScreenViewModel::class.java].loadedId
    val characterDetailScreenId = viewModelProvider[CharacterFullByIdViewModel::class.java].loadedId
    val personDetailScreenId = viewModelProvider[PersonByIdViewModel::class.java].loadedId
    val producerDetailScreenId = viewModelProvider[ProducerFullViewModel::class.java].loadedId
    var lastSelectedScreen by rememberSaveable { mutableStateOf<String?>(null) }

    navController.addOnDestinationChangedListener { _, destination, arguments ->
        if (destination.route == Screen.Detail.route) {
            currentDetailScreenId.intValue = arguments?.getInt("id") ?: 0
        }
        when (destination.route) {
            Screen.Home.route -> null
            Screen.RandomAnimeOrManga.route -> null
            Screen.Favorites.route -> null
            else -> {
                lastSelectedScreen = destination.route
            }
        }
    }


ModalNavigationDrawer(
drawerContent = {
    ShowDrawerContent(
        modifier = modifier,
        imageLoader = svgImageLoader,
        viewModelProvider = viewModelProvider,
        componentActivity = componentActivity,
        mainDb = mainDb
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
        floatingActionButtonPosition = FabPosition.Center,
        content = { padding ->
            padding.calculateTopPadding()
            SetupNavGraph(
                navController = navController,
                viewModelProvider = viewModelProvider,
                modifier = modifier,
            )
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
    viewModelProvider: ViewModelProvider,
    componentActivity: ComponentActivity,
    mainDb: MainDb
) {
    val homeScreenViewModel = viewModelProvider[HomeScreenViewModel::class.java]
    var isHelpFAQOpen by remember { mutableStateOf(false) }
    var isLegalOpen by remember { mutableStateOf(false) }
    val isExportDataPopUpDialogOpen = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val customModifier =
        modifier
            .fillMaxWidth(0.8f)
            .height(70.dp)
            .clip(CardDefaults.shape)
            .background(LightGreen)
//            .clickable {
//                try {
//                    val saveDb = homeScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
//                        // Получение пути к базе данных
//                        val dbFile = context.getDatabasePath("Main.db")
//                        // Копирование базы данных
//                        val destinationDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                        val destinationFile = File(destinationDir, "Main.db")
//                        dbFile.copyTo(destinationFile, overwrite = destinationFile.delete())
//
//                        // Повторное открытие базы данных после копирования
//                        mainDb.openHelper.writableDatabase
//                    }
//                        homeScreenViewModel.viewModelScope.launch(Dispatchers.Main) {
//                        // Ожидание завершения сохранения базы данных
//                        saveDb.join()
//
//                        // Отображение уведомления
//                        Toast.makeText(context, "Database is saved!", Toast.LENGTH_SHORT).show()
//                    }
//                    // Успешно скопировано
//                } catch (e: Exception) {
//                    homeScreenViewModel.viewModelScope.launch(Dispatchers.Main) {
//                        Toast
//                            .makeText(
//                                context,
//                                e.message,
//                                Toast.LENGTH_SHORT
//                            )
//                            .show()
//                    }
//                }
//            }

            .clickable {
                try {
                    val saveDb = homeScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
                        // Получение пути к базе данных
                        val dbFile = context.getDatabasePath("Main.db")
                        // Копирование базы данных
                        val destinationDir =
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        val destinationFile = File(destinationDir, "Main.db")

                        copyFile(dbFile, destinationFile)

                        // Повторное открытие базы данных после копирования
                        mainDb.openHelper.writableDatabase
                    }

                    homeScreenViewModel.viewModelScope.launch(Dispatchers.Main) {
                        // Ожидание завершения сохранения базы данных
                        saveDb.join()

                        // Отображение уведомления
                        Toast
                            .makeText(context, "Database is saved!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    // Успешно скопировано
                } catch (e: Exception) {
                    homeScreenViewModel.viewModelScope.launch(Dispatchers.Main) {
                        Toast
                            .makeText(context, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
//            .clickable {
//                onClickSaveData(context, homeScreenViewModel)
//            }


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
                    isHelpFAQOpen = !isHelpFAQOpen
                },
                badge = {
                    if (isHelpFAQOpen) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.arrowdown, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(17.dp)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.arrowright, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(17.dp)
                        )
                    }
                },
            )
            Divider(thickness = 3.dp)
            if (isHelpFAQOpen) {
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Known bugs",
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
                            ), contentDescription = null, modifier = modifier.size(30.dp),
                            colorFilter = ColorFilter.tint(Color(114, 114, 114, 255))
                        )
                    },
                )
                Divider(thickness = 3.dp)
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "About the features of the app",
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
                    label = {
                        Text(
                            text = "My list functional problems",
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
                    label = {
                        Text(
                            text = "Database problems",
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
                    label = {
                        Text(
                            text = "Technical problems",
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
            }
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
                    isLegalOpen = !isLegalOpen
                },
                badge = {
                    if (isLegalOpen) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.arrowdown, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(17.dp)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.arrowright, imageLoader = imageLoader
                            ), contentDescription = null, modifier = modifier.size(17.dp)
                        )
                    }
                },
            )
            Divider(thickness = 3.dp)
            if (isLegalOpen) {
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Terms of use",
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
                    label = {
                        Text(
                            text = "Resource",
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
            }
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
                    onClickRequestPermission(componentActivity, isExportDataPopUpDialogOpen)
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
                    colors = CardDefaults.cardColors(containerColor = Color.White)
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
                                fontWeight = FontWeight.ExtraBold
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
                                color = Color.White
                            )
                        }
                        Spacer(modifier = modifier.height(10.dp))
                        Row(
                            modifier = modifier
                                .fillMaxWidth(0.8f)
                                .height(70.dp)
                                .clip(CardDefaults.shape)
                                .border(4.dp, LightGreen, CardDefaults.shape)
                                .clickable {

                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Upload Data",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold
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

private fun onClickSaveData(context: Context, homeScreenViewModel: HomeScreenViewModel) {
    try {
        val dbFile = context.getDatabasePath("Main.db")
        Log.e("dbFile", dbFile.absolutePath)
        val destinationDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        Log.e("destinationDir", destinationDir.absolutePath)
        val destinationFile = File(destinationDir, "Main.db")

        // Проверяем, существует ли файл базы данных
        if (dbFile.exists()) {
            FileInputStream(dbFile).use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    // Читаем данные из исходного файла и записываем их в файл назначения
                    while (inputStream
                            .read(buffer)
                            .also { length = it } > 0
                    ) {
                        outputStream.write(buffer, 0, length)
                    }
                }
            }

            // Если файл успешно скопирован, отображаем уведомление
            homeScreenViewModel.viewModelScope.launch {
                Toast
                    .makeText(context, "Data was saved successfully!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            // Если файл базы данных не найден, отображаем сообщение об ошибке
            homeScreenViewModel.viewModelScope.launch {
                Toast
                    .makeText(
                        context,
                        "File was not found!",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }
    } catch (e: Exception) {
        // Обработка ошибок при копировании файла
        homeScreenViewModel.viewModelScope.launch {
            Toast
                .makeText(
                    context,
                    "Error when copied data: ${e.message}",
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }
}

//private suspend fun copyFile(sourceFile: File, destFile: File) = withContext(Dispatchers.IO) {
//    FileInputStream(sourceFile).channel.use { source ->
//        FileOutputStream(destFile).channel.use { destination ->
//            destination.transferFrom(source, 0, source.size())
//        }
//    }
//}

private suspend fun copyFile(source: File, destination: File) {

    suspendCoroutine<Unit> { continuation ->
        val sourceChannel = FileInputStream(source).channel
        val destChannel = FileOutputStream(destination).channel

        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size())

        sourceChannel.close()
        destChannel.close()

        continuation.resume(Unit)
    }
}