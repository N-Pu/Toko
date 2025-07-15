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
import com.project.toko.core.BottomBarViewModel
import com.project.toko.core.data.settings.DrawerViewModel
import com.project.toko.core.data.settings.darkMode.SaveDarkModeManager
import com.project.toko.core.ui.ShowDrawerContent
import com.project.toko.core.ui.theme.Theme
import com.project.toko.dataBase.search.ui.viewmodel.CatalogSearchViewModel
import com.project.toko.homeScreen.ui.homeScreen.MainScreen
import com.project.toko.homeScreen.ui.viewModel.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    @Inject
    lateinit var svgImageLoader: ImageLoader

    @Inject
    lateinit var darkThemeManager: SaveDarkModeManager

    private val drawerViewModel: DrawerViewModel by activityViewModels()

    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val isDark by darkThemeManager.isDarkThemeActive.collectAsStateWithLifecycle()
            val catalogSearchViewModel: CatalogSearchViewModel = hiltViewModel()
            val homePageViewModel: HomePageViewModel = hiltViewModel()

            val switchIndicator = remember { catalogSearchViewModel.switchIndicator }

            LaunchedEffect(drawerState.isOpen) {
                drawerViewModel.setDrawerState(drawerState.isOpen)
            }

            LaunchedEffect(key1 = switchIndicator.value) {
                if (switchIndicator.value.not()) {
                    homePageViewModel.loadAllSections(context)
                    return@LaunchedEffect
                }
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
                                toggleSFW = { catalogSearchViewModel.toggleSFW() },
                                toggleState = {
                                    catalogSearchViewModel.sfwState
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
                            catalogSearchViewModel = catalogSearchViewModel,
                            onClickFilterTypes = { selectedTypeName, types ->
                                selectedTypeName.value =
                                    if (types.name == selectedTypeName.value) null else types.name
                                catalogSearchViewModel.updateFilter { this.copy(type = selectedTypeName.value) }
                                switchIndicator.value = true
                            },
                            onClickOrderBy = { selectedFilterOrderBy, types ->

                                selectedFilterOrderBy.value =
                                    if (types.name == selectedFilterOrderBy.value) null else types.name
                                catalogSearchViewModel.updateFilter { this.copy(orderBy = selectedFilterOrderBy.value) }
                                switchIndicator.value = true

                            },
                            onClickGenres = { selectedGenreIds, genre ->

                                // Обновляем выбранные жанры
                                selectedGenreIds.value = if (genre.id in selectedGenreIds.value) {
                                    selectedGenreIds.value - genre.id
                                } else {
                                    selectedGenreIds.value + genre.id
                                }
                                // Отправляем ID жанра в ViewModel
                                catalogSearchViewModel.onGenreChange(genre.id)

                                // Обновляем индикатор изменений
                                switchIndicator.value = true
                            },
                            onClickRating = { selectedRating, rating ->

                                selectedRating.value =
                                    if (rating.name == selectedRating.value) null else rating.name
                                catalogSearchViewModel.updateFilter { this.copy(rating = selectedRating.value) }
                                switchIndicator.value = true
                            },

                            onCurrentScore = { range ->
                                catalogSearchViewModel.updateFilter {
                                    this.copy(
                                        min_score = range.minScore,
                                        max_score = range.maxScore
                                    )
                                }
                            },
                            switchIndicator = switchIndicator,
                            hideBottomBar = { bottomBarViewModel.hideBottomBar() },
                            showBottomBar = { bottomBarViewModel.showBottomBar() },
                            getTrendingAnime = {
                                homePageViewModel.topTrendingAnime.collectAsStateWithLifecycle().value
                            },
                            getTopAiring = {
                                homePageViewModel.topAiringAnime.collectAsStateWithLifecycle().value
                            },
                            getTopUpcoming = {
                                homePageViewModel.topUpcomingAnime.collectAsStateWithLifecycle().value
                            },
                        )
                    }

                }
            }
        }
    }
}
