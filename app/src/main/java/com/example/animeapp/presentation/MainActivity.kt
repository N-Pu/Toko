package com.example.animeapp.presentation

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.animeapp.repository.MalApiService
import com.example.animeapp.presentation.appConstraction.TokoAppActivator
import com.example.animeapp.presentation.theme.AnimeAppTheme
import com.example.animeapp.presentation.theme.MainBackgroundColor
import com.example.animeapp.presentation.theme.LightYellow

import com.example.animeapp.domain.viewModel.viewModelFactory.MyViewModelFactory



class MainActivity : ComponentActivity() {


    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noTitleBarAndSplashScreenActivator()

        setContent {
            AnimeAppTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MainBackgroundColor


                ) {
                    installSplashScreen() // Custom Splash Screen
                    navController = rememberNavController()

                    val myViewModelFactory = MyViewModelFactory(MalApiService)
                    val viewModel = ViewModelProvider(this, myViewModelFactory)

                    TokoAppActivator(navController = navController, viewModelProvider = viewModel)



//                    System.setProperty("webdriver.ie.driver", "IEDriverServer.exe")
//                    val driver: WebDriver = ChromeDriver()
//                    val link = driver.get("https://myanimelist.net/news")
//                    driver.get("chrome://settings/clearBrowserData")
//                    Log.d("MAIN-SELENIUM", link.toString())


                }
            }
        }
    }


    private fun noTitleBarAndSplashScreenActivator() {
        window.navigationBarColor = LightYellow.toArgb()
        requestWindowFeature(Window.FEATURE_NO_TITLE) // Undo topBar
        installSplashScreen() // Custom Splash Screen
    }
}







