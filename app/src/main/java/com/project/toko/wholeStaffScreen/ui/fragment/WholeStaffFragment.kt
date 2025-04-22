package com.project.toko.wholeStaffScreen.ui.fragment

import android.os.Bundle
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
import com.project.toko.R
import com.project.toko.core.data.settings.SaveDarkModeManager
import com.project.toko.core.ui.theme.Theme
import com.project.toko.detailScreen.ui.detailScreen.sideContent.staffList.ShowWholeStaff
import com.project.toko.detailScreen.ui.viewModel.DetailScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WholeStaffFragment : Fragment(R.layout.fragment_whole_staff) {
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
            val isDark = darkThemeManager.isDarkThemeActive.value

            Theme(
                darkTheme = isDark,
                systemUiController = rememberSystemUiController()
            ) {
                val navController = remember {
                    findNavController()
                }
                val viewModel: DetailScreenViewModel = hiltViewModel()
                ShowWholeStaff(
                    onNavigateToDetailOnStaff = { staffId ->
                        navController.navigate(
                            R.id.action_wholeStaff_to_singleStaffFragment,
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