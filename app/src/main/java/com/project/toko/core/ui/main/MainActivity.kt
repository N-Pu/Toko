package com.project.toko.core.ui.main


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.toko.R
import com.project.toko.core.data.settings.DrawerViewModel
import com.project.toko.core.data.settings.SaveDarkModeManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
//    private val drawerViewModel: DrawerViewModel by viewModels()

    @Inject
    lateinit var darkThemeManager: SaveDarkModeManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupNavigation()
        observeThemeChanges()
    }

private fun setupNavigation() {
    bottomNav = findViewById(R.id.bottom_nav)
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    bottomNav.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_UNLABELED

    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    val navController = navHostFragment.navController

    // Анимация + навигация вручную
    bottomNav.setOnItemSelectedListener { item ->
        val handled = NavigationUI.onNavDestinationSelected(item, navController)
        if (handled) {
            val menuView = bottomNav.getChildAt(0) as? ViewGroup
            menuView?.let {
                for (i in 0 until it.childCount) {
                    val itemView = it.getChildAt(i)
                    val menuItem = bottomNav.menu.getItem(i)
                    if (menuItem.itemId == item.itemId) {
                        itemView.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).withEndAction {
                            itemView.animate().scaleX(1f).scaleY(1f).duration = 150
                        }.start()
                        break
                    }
                }
            }
        }
        handled
    }

    // Скрытие BottomNav на некоторых экранах
    navController.addOnDestinationChangedListener { _, destination, _ ->
        when (destination.id) {
            R.id.detailScreenFragment,
            R.id.singleCharacterFragment,
            R.id.singleStaffFragment,
            R.id.wholeCast,
            R.id.wholeStaff -> hideBottomNav()
            else -> showBottomNav()
        }
    }

    // Drawer наблюдение
//    lifecycleScope.launch {
//        drawerViewModel.isDrawerOpen.collect { isOpen ->
//            if (isOpen) hideBottomNav() else showBottomNav()
//        }
//    }
}

    private fun observeThemeChanges() {
        lifecycleScope.launch {
            darkThemeManager.isDarkThemeActive.collect { isDarkTheme ->
                applyTheme(isDarkTheme)
            }
        }
    }
    private fun applyTheme(isDarkTheme: Boolean) {
       if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        delegate.applyDayNight() // Немедленное применение
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

