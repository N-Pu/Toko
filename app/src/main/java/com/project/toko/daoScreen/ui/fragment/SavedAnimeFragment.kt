package com.project.toko.daoScreen.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
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
import com.project.toko.daoScreen.ui.screen.DaoScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedAnimeFragment
    : Fragment(
    R.layout.fragment_saved_anime
) {

    @Inject
    lateinit var imageLoader: ImageLoader

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

            // Следим за drawerState и обновляем ViewModel
            LaunchedEffect(drawerState.isOpen) {
                drawerViewModel.setDrawerState(drawerState.isOpen)
            }

            Theme(
                darkTheme = isDark,
                systemUiController = rememberSystemUiController()
            ) {
                ModalNavigationDrawer(drawerState = drawerState,
                    modifier = Modifier,
                    drawerContent = {


                        ShowDrawerContent(imageLoader = imageLoader,
                            onThemeChange = {
                                darkThemeManager.toggleTheme()
                            }, darkTheme = { isDark },
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.9f)
                        )
                    }
                ) {
                    val navController = remember { findNavController() }

                    DaoScreen(
                        onNavigateToDetailOnCharacter = { characterId ->
                            navController.navigate(
                                R.id.action_savedAnimeFragment_to_singleCharacterFragment,
                                bundleOf("single_character_id" to characterId),
                            )
                        }, onNavigateToDetailOnStaff = { staffId ->
                            navController.navigate(
                                R.id.action_savedAnimeFragment_to_singleStaffFragment,
                                bundleOf("single_staff_id" to staffId)
                            )
                        }, onNavigateToDetailScreen = { detailScreenId ->
                            navController.navigate(
                                R.id.action_savedAnimeFragment_to_detailScreenFragment,
                                bundleOf("detail_screen_id" to detailScreenId),
                            )
                        },
                        isInDarkTheme = { isDark },
                        drawerState = drawerState,
                        svgImageLoader = { imageLoader }
                    )
                }


            }


        }

    }

}



