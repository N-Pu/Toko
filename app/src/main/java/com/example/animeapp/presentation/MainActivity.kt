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
import com.example.animeapp.domain.repository.MalApiService
import com.example.animeapp.presentation.appConstraction.TokoAppActivator
import com.example.animeapp.presentation.theme.AnimeAppTheme
import com.example.animeapp.presentation.theme.MainBackgroundColor
import com.example.animeapp.presentation.theme.LightYellow

import com.example.animeapp.viewModel.viewModelFactory.MyViewModelFactory


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

                    val myViewModelFactory = MyViewModelFactory(MalApiService.Companion)
                    val viewModel = ViewModelProvider(this,myViewModelFactory)
//                   val d = viewModel.get(StaffViewModel::class.java)

//                    val searchViewModel = viewModel<HomeScreenViewModel>()
//                    val detailViewModel = viewModel<DetailScreenViewModel>()
//                    val castViewModel = viewModel<CharactersViewModel>()
//                    val staffViewModel = viewModel<StaffViewModel>()
//                    val idViewModel = viewModel<IdViewModel>()
//                    val savedAnimeViewModel = viewModel<SavedAnimeViewModel>()
                    TokoAppActivator(
                        navController = navController,
                        viewModelProvider = viewModel
//                        savedAnimeViewModel = savedAnimeViewModel
                    )
//                    SetupNavGraph(navController = navController)

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







