package com.project.toko.core.ui.main


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.toko.R
import com.project.toko.core.data.settings.DrawerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    private val drawerViewModel: DrawerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
//        window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            bottomNav = findViewById(R.id.bottom_nav)
//        requestedOrientation =
//            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // locked screen for the time being
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

            bottomNav.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_UNLABELED

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
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
                    R.id.detailScreenFragment,
                    R.id.singleCharacterFragment,
                    R.id.singleStaffFragment,
                    R.id.wholeCast,
                    R.id.wholeStaff -> hideBottomNav()

                    else -> showBottomNav()
                }
            }
            // Подписка на drawer state
            lifecycleScope.launch {
                drawerViewModel.isDrawerOpen.collect { isOpen ->
                    if (isOpen) hideBottomNav() else showBottomNav()
                }
            }


            bottomNav.setupWithNavController(navController)
        }
    }

    private fun hideBottomNav() {
        bottomNav.clearAnimation()
        bottomNav.animate().translationY(bottomNav.height.toFloat()).alpha(0f).setDuration(300)
            .withEndAction { bottomNav.visibility = View.GONE }.start()
    }

    private fun showBottomNav() {
        bottomNav.visibility = View.VISIBLE
        bottomNav.clearAnimation()
        bottomNav.animate().translationY(0f).alpha(1f).setDuration(300).start()
    }

}
