package com.project.toko

import android.os.Bundle
import android.telecom.Call.Details
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.findNavController
import coil.ImageLoader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.project.toko.core.data.settings.SaveDarkModeManager
import com.project.toko.core.ui.theme.Theme
import com.project.toko.detailScreen.ui.detailScreen.sideContent.castList.ShowWholeCast
import com.project.toko.detailScreen.ui.viewModel.DetailScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WholeCastFragment : Fragment(R.layout.fragment_whole_cast) {
    @Inject
    lateinit var svgImageLoader: ImageLoader

    @Inject
    lateinit var darkThemeManager: SaveDarkModeManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val isDark = darkThemeManager.isDarkThemeActive.value

                Theme(
                    darkTheme = isDark,
                    systemUiController = rememberSystemUiController()
                ) {
                    val navController = remember {
                        findNavController()
                    }
                    val viewModel: DetailScreenViewModel = hiltViewModel()
                    ShowWholeCast(
                        onNavigateToDetailOnCharacter = { characterId ->

                            navController.navigate(
                                R.id.action_wholeCast_to_singleCharacterFragment,
                                bundleOf("single_character_id" to characterId)
                            )
                        },
                        onNavigateToDetailOnStaff = { staffId ->
                            navController.navigate(
                                R.id.action_wholeCast_to_singleStaffFragment,
                                bundleOf("single_staff_id" to staffId)
                            )
                        },
                        onNavigateBack = { navController.navigateUp() },
                        viewModel = viewModel,
                        modifier = Modifier,
                        isInDarkTheme = { isDark }
                    )
                }
            }
        }
    }
}