package com.project.toko

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import coil.ImageLoader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.project.toko.characterDetailedScreen.ui.characterFull.DisplayCharacterFromId
import com.project.toko.core.data.settings.SaveDarkModeManager
import com.project.toko.core.ui.theme.Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SingleCharacterFragment : Fragment(R.layout.fragment_single_character) {

    @Inject
    lateinit var svgImageLoader: ImageLoader

    @Inject
    lateinit var darkThemeManager: SaveDarkModeManager

    private val characterId by lazy {
        requireArguments().getInt("single_character_id")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val isDark = darkThemeManager.isDarkThemeActive.value
                val navController = remember { findNavController() }
                Theme(
                    darkTheme = isDark,
                    systemUiController = rememberSystemUiController()
                ) {
                    DisplayCharacterFromId(
                        id = characterId,
                        onNavigateToStaff = { staffId ->
                            navController.navigate(
                                R.id.action_singleCharacterFragment_to_singleStaffFragment,
                                bundleOf("single_staff_id" to staffId),
                            )
                        },
                        onNavigateToDetailScreen = { detailScreenId ->
                            navController.navigate(
                                R.id.action_singleCharacterFragment_to_detailScreenFragment,
                                bundleOf("detail_screen_id" to detailScreenId),
                            )
                        },
                        onNavigateBack = { navController.navigateUp() },
                        modifier = Modifier,
                        isInDarkTheme = { isDark },
                        svgImageLoader = svgImageLoader
                    )
                }
            }
        }
    }
}