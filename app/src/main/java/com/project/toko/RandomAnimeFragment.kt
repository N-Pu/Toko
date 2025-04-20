package com.project.toko

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import coil.ImageLoader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.project.toko.core.data.settings.SaveDarkModeManager
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

//    private val navOptions = NavOptions.Builder()
//        .setPopEnterAnim(R.anim.slide_in_right)
//        .setPopExitAnim(R.anim.slide_out_left)
//        .setPopUpTo(R.id.homeFragment, inclusive = true)
//        .build()


    @SuppressLint("ContextCastToActivity")
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
                    ShowRandomAnime(
                        onNavigateToDetailScreen = { detailScreenId ->
                            navController.navigate(
                                R.id.action_randomAnimeFragment_to_detailScreenFragment,
                                bundleOf("detail_screen_id" to detailScreenId),
                            )

                        },
                    )
                }

//                BackHandler {
//                    Log.d("RANDOM SCREEN", "NAVIGATED UP")
////                    navController.navigate(R.id.homeFragment, navOptions = navOptions)
//                    navController.popBackStack()
//                }

//                BackHandler {
//
//                    // Находим NavController из активности
//                    val activityNavController =
//                        (activity as? AppCompatActivity)?.findNavController(R.id.nav_host_fragment)
//                    Log.d("RANDOM SCREEN", "R.id.action_randomAnimeFragment_to_homeFragment")
//                    activityNavController?.navigate(R.id.action_randomAnimeFragment_to_homeFragment) {
//                        // Очищаем back stack до homeFragment
//                        popUpTo(R.id.nav_graph) {
//                            inclusive = false
//                        }
//                    }
//                }

//
//                BackHandler {
//                    navController.navigate(R.id.homeFragment, null, navOptions)
//                }
            }
            }
        }
    }