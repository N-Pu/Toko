package com.project.toko.homeScreen.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.findNavController
import coil.ImageLoader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.project.toko.R
import com.project.toko.core.ui.ShowDrawerContent
import com.project.toko.core.data.settings.DrawerViewModel
import com.project.toko.core.data.settings.SaveDarkModeManager
import com.project.toko.core.ui.theme.Theme
import com.project.toko.homeScreen.ui.homeScreen.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    @Inject
    lateinit var svgImageLoader: ImageLoader

    @Inject
    lateinit var darkThemeManager: SaveDarkModeManager

    private val drawerViewModel: DrawerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
            setContent {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val isDark by darkThemeManager.isDarkThemeActive.collectAsStateWithLifecycle()

                LaunchedEffect(drawerState.isOpen) {
                    drawerViewModel.setDrawerState(drawerState.isOpen)
                }

                Theme(
                    darkTheme = isDark, systemUiController = rememberSystemUiController()
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.systemBars)
                    ) {
                        ModalNavigationDrawer(
                            modifier = Modifier.statusBarsPadding(),
                            drawerState = drawerState,
                            drawerContent = {
                                ShowDrawerContent(
                                    imageLoader = svgImageLoader,
                                    onThemeChange = { darkThemeManager.toggleTheme() },
                                    darkTheme = { isDark },
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(0.9f)
                                )
                            }
                        ) {
                            val navController = remember { findNavController() }
                            MainScreen(
                                onNavigateToDetailScreen = { detailScreenId ->
                                    navController.navigate(
                                        R.id.action_homeFragment_to_detailScreenFragment,
                                        bundleOf("detail_screen_id" to detailScreenId)
                                    )
                                },
                                isInDarkTheme = { isDark },
                                drawerState = drawerState,
                                svgImageLoader = { svgImageLoader }
                            )
                        }
                    }
                }
            }
        }
}


// Storage Permissions
//private const val REQUEST_EXTERNAL_STORAGE = 1
//private val PERMISSIONS_STORAGE = arrayOf(
//    Manifest.permission.READ_EXTERNAL_STORAGE,
//    Manifest.permission.WRITE_EXTERNAL_STORAGE
//)

//private fun onClickRequestPermission(
//    componentActivity: ComponentActivity,
//    isExportDataPopUpDialogOpen: MutableState<Boolean>
//) {
//
//    when {
//        ContextCompat.checkSelfPermission(
//            componentActivity.applicationContext,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED -> {
//            isExportDataPopUpDialogOpen.value = true
//        }
//
//        ActivityCompat.shouldShowRequestPermissionRationale(
//            componentActivity,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ) -> {
//            ActivityCompat.requestPermissions(
//                componentActivity,
//                PERMISSIONS_STORAGE,
//                REQUEST_EXTERNAL_STORAGE
//            )
//        }
//
//        else -> {
//            Toast
//                .makeText(
//                    componentActivity,
//                    "Permission Denied!",
//                    Toast.LENGTH_SHORT
//                )
//                .show()
//            ActivityCompat.requestPermissions(
//                componentActivity,
//                PERMISSIONS_STORAGE,
//                REQUEST_EXTERNAL_STORAGE
//            )
//        }
//    }
//}

//    @Composable
//    fun AnimeListTypesToDelete(
//        daoViewModel: DaoViewModel, modifier: Modifier
//    ) {
//        val animeListTypes = AnimeStatus.values()
//        val isDeleteDataOpen = remember { mutableStateOf(false) }
//        val currentSelectedAnimeListType = daoViewModel.currentSelectedAnimeListType
//        val customModifier = modifier
//            .fillMaxWidth(0.8f)
//            .height(70.dp)
//            .clip(CardDefaults.shape)
//            .background(MaterialTheme.colorScheme.onPrimaryContainer)
//
//
//        animeListTypes.forEach { type ->
//            NavigationDrawerItem(
//                colors = NavigationDrawerItemDefaults.colors(
//                    selectedContainerColor = MaterialTheme.colorScheme.surfaceTint,
//                    unselectedContainerColor = MaterialTheme.colorScheme.surfaceTint
//                ),
//                label = {
//                    Text(
//                        text = "Delete " + type.route,
//                        fontWeight = FontWeight.ExtraBold,
//                        fontSize = 22.sp,
//                        modifier = Modifier.padding(start = 20.dp),
//                        color = MaterialTheme.colorScheme.onPrimary,
//                        fontFamily = evolventaBoldFamily
//                    )
//                },
//                selected = false,
//                onClick = {
//                    currentSelectedAnimeListType.value = type.route
//                    isDeleteDataOpen.value = true
//                },
//                badge = {
//                    Icon(
//                        imageVector = Icons.Filled.Delete,
//                        contentDescription = "Delete ${type.route}",
//                        modifier = Modifier.size(30.dp),
//                        tint = MaterialTheme.colorScheme.onPrimary
//                    )
//                },
//            )
//            Divider(thickness = 3.dp, color = MaterialTheme.colorScheme.onSurface)
//
//
//            if (isDeleteDataOpen.value) {
//                DeleteDialog(modifier,
//                    customModifier,
//                    isDeleteDataOpen,
//                    currentSelectedAnimeListType.value,
//                    deleteProcess = {
//                        when (currentSelectedAnimeListType.value) {
//                            AnimeStatus.FAVORITE.route -> {
//                                daoViewModel.viewModelScope.launch {
//                                    daoViewModel.deleteAllFavorite()
//                                }
//                            }
//
//                            AnimeStatus.PERSON.route -> {
//                                daoViewModel.viewModelScope.launch {
//                                    daoViewModel.deleteAllPeople()
//                                }
//                            }
//
//                            AnimeStatus.CHARACTER.route -> {
//                                daoViewModel.viewModelScope.launch {
//                                    daoViewModel.deleteAllCharacters()
//                                }
//                            }
//
//                            else -> {
//                                daoViewModel.viewModelScope.launch {
//                                    daoViewModel.deleteAnimeByCategory(currentSelectedAnimeListType.value)
//                                }
//                            }
//                        }
//
//                    })
//            }
//        }
//    }


//    @Composable
//    fun DeleteDialog(
//        modifier: Modifier,
//        customModifier: Modifier,
//        isExportDataPopUpDialogOpen: MutableState<Boolean>,
//        animeListType: String,
//        deleteProcess: () -> Unit
//    ) {
//        Dialog(
//            onDismissRequest = {
//                isExportDataPopUpDialogOpen.value = false
//            }, properties = DialogProperties(
//                dismissOnBackPress = true,
//                dismissOnClickOutside = true,
//            )
//        ) {
//            Box(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.4f),
//                contentAlignment = Alignment.Center
//            ) {
//                Card(
//                    modifier = modifier.fillMaxSize(),
//                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceTint)
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.SpaceAround,
//                        modifier = modifier.fillMaxSize()
//                    ) {
//                        Row(
//                            modifier = modifier.fillMaxHeight(0.4f),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Text(
//                                text = "Delete $animeListType data?",
//                                fontSize = 25.sp,
//                                fontWeight = FontWeight.ExtraBold,
//                                color = MaterialTheme.colorScheme.onPrimary,
//                                fontFamily = evolventaBoldFamily
//                            )
//                        }
//                        Row(
//                            modifier = customModifier.clickable {
//                                deleteProcess()
//                                isExportDataPopUpDialogOpen.value = false
//                            },
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Text(
//                                text = "Yes",
//                                fontSize = 22.sp,
//                                fontWeight = FontWeight.ExtraBold,
//                                color = Color.White,
//                                fontFamily = evolventaBoldFamily
//                            )
//                        }
//                        Spacer(modifier = modifier.height(10.dp))
//                        Row(
//                            modifier = modifier
//                                .fillMaxWidth(0.8f)
//                                .height(70.dp)
//                                .clip(CardDefaults.shape)
//                                .border(
//                                    4.dp,
//                                    MaterialTheme.colorScheme.onPrimaryContainer,
//                                    CardDefaults.shape
//                                )
//                                .clickable {
//                                    isExportDataPopUpDialogOpen.value = false
//                                },
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Text(
//                                text = "No",
//                                fontSize = 22.sp,
//                                fontWeight = FontWeight.ExtraBold,
//                                color = MaterialTheme.colorScheme.onPrimary,
//                                fontFamily = evolventaBoldFamily
//                            )
//                        }
//                        Spacer(modifier = modifier.height(20.dp))
//                    }
//                }
//            }
//        }
//    }





