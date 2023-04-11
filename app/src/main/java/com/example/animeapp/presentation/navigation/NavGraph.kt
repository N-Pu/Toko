package com.example.animeapp.presentation.navigation


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.animeapp.presentation.firstScreen.MainScreenSearchPanel
import com.example.animeapp.presentation.secondScreen.DetailScreen


@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            MainScreenSearchPanel(navController)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })) {
            DetailScreen(navController = navController)
            Log.d("Arg", it.arguments?.getInt("id").toString())

        }
    }
}

//@Composable
//fun Screen1(navController: NavHostController) {
//    // Использование navController внутри композабельной функции
//
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(Color.Blue)
//        .clickable {
//            navController.navigate("screen2")
//        }) {
//
//    }
//
//}
//
//@Composable
//fun Screen2(navController: NavHostController) {
//    // Использование navController внутри композабельной функции
//    Button(onClick = { navController.navigate("screen1") }) {
//        Text("Go to Screen 1")
//    }
//}
//
//@Composable
//fun MyApp() {
//    val navController = rememberNavController()
//
//    NavHost(navController, startDestination = "screen1") {
//        composable("screen1") { Screen1(navController) }
//        composable("screen2") { Screen2(navController) }
//    }
//}
