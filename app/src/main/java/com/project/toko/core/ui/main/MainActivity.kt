package com.project.toko.core.ui.main


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.forEach
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.toko.R
import com.project.toko.core.data.settings.DrawerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//@AndroidEntryPoint
//class MainActivity : AppCompatActivity() {
//
//
////    lateinit var navController: NavHostController
//
//    @Inject
//    lateinit var svgImageLoader: ImageLoader
//
//    private lateinit var darkTheme: SaveDarkMode
//
//    private lateinit var navController: NavController
//    private lateinit var bottomNav: BottomNavigationView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        supportActionBar?.hide()
//        setContentView(R.layout.activity_main) // <<< ЭТО ОБЯЗАТЕЛЬНО
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//
//        requestedOrientation =
//            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // locked screen for the time being
//
//        darkTheme = SaveDarkMode(this)
//        darkTheme.loadData()
//
//        // Инициализация navHostFragment
//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
//        if (navHostFragment != null) {
//            navController = navHostFragment.navController
//        }
//        bottomNav = findViewById(R.id.bottom_nav)
//        bottomNav.setupWithNavController(navController)
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.savedAnimeFragment -> hideBottomNav()
//                else -> showBottomNav()
//            }
//        }
//
//    }
//    fun hideBottomNav() {
//        bottomNav.animate()
//            .translationY(bottomNav.height.toFloat())
//            .alpha(0f)
//            .setDuration(1000)
//            .withEndAction {
//                bottomNav.visibility = View.GONE
//            }
//            .start()
//    }
//
//    fun showBottomNav() {
//        bottomNav.visibility = View.VISIBLE
//        bottomNav.animate()
//            .translationY(0f)
//            .alpha(1f)
//            .setDuration(1000)
//            .start()
//    }
//
//}
//

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    private val drawerViewModel: DrawerViewModel by viewModels()
//    private val customDialogViewModel: CustomDialogViewModel by viewModels()

//    private val bottomNavVisibilityViewModel: BottomNavVisibilityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        bottomNav = findViewById(R.id.bottom_nav)
        requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // locked screen for the time being
        bottomNav.labelVisibilityMode =
            BottomNavigationView.LABEL_VISIBILITY_UNLABELED

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { controller, destination, _ ->

            Log.d("NavDebug", "Current destination: ${destination.label}")

            val childFragmentManager = navHostFragment.childFragmentManager
            val count = childFragmentManager.backStackEntryCount
            Log.d("NavDebug", "Back stack entry count: $count")

            for (i in 0 until count) {
                val entry = childFragmentManager.getBackStackEntryAt(i)
                Log.d("NavDebug", "[$i]: ${entry.name}")
            }
            val current = controller.currentBackStackEntry
            val previous = controller.previousBackStackEntry

            Log.d("NavDebug", "Current destination: ${destination.label}")
            Log.d("NavDebug", "Current entry: ${current?.destination?.label}")
            Log.d("NavDebug", "Previous entry: ${previous?.destination?.label}")

            when (destination.id) {
                R.id.detailScreenFragment, R.id.singleCharacterFragment, R.id.singleStaffFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
        // Подписка на drawer state
        lifecycleScope.launch {
            drawerViewModel.isDrawerOpen.collect { isOpen ->
                if (isOpen) hideBottomNav() else showBottomNav()
            }
        }


//    lifecycleScope.launch {
//        bottomNavVisibilityViewModel.isBottomNavVisible.collect { isVisible ->
//            if (isVisible) showBottomNav() else hideBottomNav()
//        }
//    }
        bottomNav.setupWithNavController(navController)
    }

    private fun hideBottomNav() {
        bottomNav.clearAnimation()
        bottomNav.animate()
            .translationY(bottomNav.height.toFloat())
            .alpha(0f)
            .setDuration(300)
            .withEndAction { bottomNav.visibility = View.GONE }
            .start()
    }

    private fun showBottomNav() {
        bottomNav.visibility = View.VISIBLE
        bottomNav.clearAnimation()
        bottomNav.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(300)
            .start()
    }

}


//        setContent {
//            val systemUiController = rememberSystemUiController(window)
//            val splashShown = remember { mutableStateOf(false) }
//            navController = rememberNavController()
//            if (!splashShown.value) {
//
//                systemUiController.isNavigationBarVisible = false
//                SplashTheme(
//                    darkTheme = darkTheme.isDarkThemeActive.value,
//                    systemUiController = systemUiController
//                ) {
//                    AnimatedSplashScreen(navController, svgImageLoader) {
//                        splashShown.value = true
//                    }
//                }
//            } else {
//                systemUiController.isNavigationBarVisible = true
//                Theme(
//                    darkTheme = darkTheme.isDarkThemeActive.value,
//                    systemUiController = systemUiController
//                ) {
//                    // A surface container using the 'background' color from the theme
//                    Surface(
//                        modifier = Modifier
//                            .windowInsetsPadding(NavigationBarDefaults.windowInsets)
//                            .fillMaxSize(),
//                    ) {
//                        AppActivator(
//                            navController = navController,
//                            modifier = Modifier,
//                            componentActivity = this,
//                            onThemeChange = {
//                                darkTheme.isDarkThemeActive.value =
//                                    !darkTheme.isDarkThemeActive.value
//                                darkTheme.saveData(darkTheme.isDarkThemeActive.value)
//                            },
//                            isInDarkTheme = { darkTheme.isDarkThemeActive.value },
//                            svgImageLoader = svgImageLoader
//                        )
//                    }
//                }
//            }
//        }