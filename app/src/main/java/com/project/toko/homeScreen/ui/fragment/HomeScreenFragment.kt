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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.project.toko.R
import com.project.toko.core.MainViewModel
import com.project.toko.core.data.settings.DrawerViewModel
import com.project.toko.core.data.settings.darkMode.SaveDarkModeManager
import com.project.toko.core.ui.ShowDrawerContent
import com.project.toko.core.ui.theme.Theme
import com.project.toko.dataBase.search.ui.viewmodel.AnimeViewModel
import com.project.toko.homeScreen.ui.homeScreen.MainScreen
import com.project.toko.homeScreen.ui.viewModel.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    @Inject
    lateinit var svgImageLoader: ImageLoader

    @Inject
    lateinit var darkThemeManager: SaveDarkModeManager

    private val drawerViewModel: DrawerViewModel by activityViewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val isDark by darkThemeManager.isDarkThemeActive.collectAsStateWithLifecycle()
            val animeViewModel: AnimeViewModel = hiltViewModel()
            val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()

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
                                    .fillMaxWidth(0.9f),
                                toggleSFW = { animeViewModel.toggleSFW()},
                                toggleState = {
                                    animeViewModel.sfwState
                                }
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
                            svgImageLoader = { svgImageLoader },
                            animeViewModel = animeViewModel,
                            viewModel= homeScreenViewModel,
                            mainViewModel = mainViewModel

                        )
                    }

                }
            }
        }
    }
}
