package com.project.toko.core.presentation_layer


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.project.toko.core.dao.MainDb
import com.project.toko.core.di.Application
import com.project.toko.core.repository.MalApiService
import com.project.toko.core.presentation_layer.appConstraction.AppActivator
import com.project.toko.core.presentation_layer.theme.SplashTheme
import com.project.toko.core.presentation_layer.theme.Theme
import com.project.toko.core.settings.SaveDarkMode
import com.project.toko.core.viewModel.viewModelFactory.MyViewModelFactory
import com.project.toko.homeScreen.viewModel.HomeScreenViewModel
import com.project.toko.splashScreen.AnimatedSplashScreen
import javax.inject.Inject


class MainActivity : ComponentActivity() {


    lateinit var navController: NavHostController

    @Inject
    lateinit var mainDb: MainDb

    @Inject
    lateinit var modifier: Modifier

    @Inject
    lateinit var malApi: MalApiService

    private lateinit var darkTheme: SaveDarkMode
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // locked screen for the time being

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        darkTheme = SaveDarkMode(this)
        darkTheme.loadData()

        val modifierComponent = (application as Application).modifierComponent
        val databaseComponent = (application as Application).daoComponent
        val malApiComponent = (application as Application).malApiComponent
        val context = (application as Application).context.context
        modifier = modifierComponent.providesModifier()
        mainDb = databaseComponent.provideDao()
        malApi = malApiComponent.provideMalApiService()
        val myViewModelFactory =
            MyViewModelFactory(
                malApiRepository = malApi,
                mainDb = mainDb,
                context = context
            )
        val viewModelProvider = ViewModelProvider(this, myViewModelFactory)
        val svgImageLoader = ImageLoader.Builder(this).components {
            add(SvgDecoder.Factory())
        }.build()
        viewModelProvider[HomeScreenViewModel::class.java].loadNSFWData()
        setContent {
            val systemUiController = rememberSystemUiController(window)
            val splashShown = remember { mutableStateOf(false) }
            navController = rememberNavController()
            if (!splashShown.value) {
                SplashTheme(
                    darkTheme = darkTheme.isDarkThemeActive.value,
                    systemUiController = systemUiController
                ) {
                    AnimatedSplashScreen(navController, svgImageLoader) {
                        splashShown.value = true
                    }
                }
            } else {
                Theme(
                    darkTheme = darkTheme.isDarkThemeActive.value,
                    systemUiController = systemUiController
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = modifier
                            .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                            .fillMaxSize(),
                    ) {
                        AppActivator(
                            navController = navController,
                            viewModelProvider = viewModelProvider,
                            modifier = modifier,
                            componentActivity = this,
                            onThemeChange = {
                                darkTheme.isDarkThemeActive.value =
                                    !darkTheme.isDarkThemeActive.value
                                darkTheme.saveData(darkTheme.isDarkThemeActive.value)
                            },
                            isInDarkTheme = { darkTheme.isDarkThemeActive.value },
                            svgImageLoader = svgImageLoader
                        )
                    }
                }
            }
        }
    }
}
