package com.project.toko.randomAnimeScreen.presentation_layer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.findNavController
import coil.ImageLoader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.project.toko.R
import com.project.toko.core.data.settings.darkMode.SaveDarkModeManager
import com.project.toko.core.ui.theme.Theme
import com.project.toko.randomAnimeScreen.presentation_layer.randomAnimeScreen.ShowRandomAnime
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RandomAnimeFragment : Fragment(
    R.layout.fragment_random_anime
) {
    @Inject
    lateinit var svgImageLoader: ImageLoader

    @Inject
    lateinit var darkThemeManager: SaveDarkModeManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val isDark by darkThemeManager.isDarkThemeActive.collectAsStateWithLifecycle()
            Theme(
                darkTheme = isDark,
                systemUiController = rememberSystemUiController()
            ) {
                val navController = remember {
                    findNavController()
                }
                ShowRandomAnime(
                    onNavigateToDetailScreen = { detailScreenId ->
                        navController.navigate(
                            R.id.action_randomAnimeFragment_to_detailScreenFragment,
                            bundleOf("detail_screen_id" to detailScreenId),
                        )

                    },
                )
            }

        }
    }

}