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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.animeapp.presentation.theme.AnimeAppTheme
import com.example.animeapp.presentation.theme.MainBackgroundColor
import com.example.animeapp.presentation.navigation.SetupNavGraph
import com.example.animeapp.presentation.theme.LightYellow


class MainActivity : ComponentActivity() {



    lateinit var navController: NavHostController
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
                    SetupNavGraph(navController = navController)

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







