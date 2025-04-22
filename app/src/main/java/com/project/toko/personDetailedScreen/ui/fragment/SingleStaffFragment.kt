package com.project.toko.personDetailedScreen.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import coil.ImageLoader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.project.toko.R
import com.project.toko.core.data.settings.SaveDarkModeManager
import com.project.toko.core.ui.theme.Theme
import com.project.toko.personDetailedScreen.ui.staffMemberFull.DisplayPersonFullScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SingleStaffFragment : Fragment(R.layout.fragment_single_staff) {
    @Inject
    lateinit var svgImageLoader: ImageLoader

    @Inject
    lateinit var darkThemeManager: SaveDarkModeManager
    private val staffId by lazy {
        requireArguments().getInt("single_staff_id")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val isDark = darkThemeManager.isDarkThemeActive.value
            Theme(
                darkTheme = isDark,
                systemUiController = rememberSystemUiController()
            ) {
                val navController = remember { findNavController() }
                DisplayPersonFullScreen(
                    id = staffId,
                    onNavigateToDetailScreen = { detailId ->
                        navController.navigate(
                            R.id.action_singleStaffFragment_to_detailScreenFragment,
                            bundleOf("detail_screen_id" to detailId)
                        )
                    },
                    onNavigateToDetailOnCharacter = { characterId ->
                        navController.navigate(
                            R.id.action_singleStaffFragment_to_singleCharacterFragment,
                            bundleOf("single_character_id" to characterId)
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