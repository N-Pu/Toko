package com.example.animeapp.presentation

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.animeapp.presentation.theme.AnimeAppTheme
import com.example.animeapp.presentation.theme.MainBackgroundColor
import com.example.animeapp.presentation.navigation.SetupNavGraph


class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MainBackgroundColor


                ) {
                    navController = rememberNavController()
                    SetupNavGraph(navController = navController)

//                    Log.d("main", lifecycle.currentState.name)
//                    when (lifecycle.currentState) {
//                        Lifecycle.State.DESTROYED -> Log.d("main", lifecycle.currentState.name)
//                        Lifecycle.State.INITIALIZED -> Log.d("main", lifecycle.currentState.name)
//                        Lifecycle.State.CREATED -> Log.d("main", lifecycle.currentState.name)
//                        Lifecycle.State.STARTED -> Log.d("main", lifecycle.currentState.name)
//                        Lifecycle.State.RESUMED -> Log.d("main", lifecycle.currentState.name)
//                    }

                }
            }
        }
    }
}




