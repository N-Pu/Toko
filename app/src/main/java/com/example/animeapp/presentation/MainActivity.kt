package com.example.animeapp.presentation


import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.animeapp.repository.MalApiService
import com.example.animeapp.presentation.appConstraction.TokoAppActivator
import com.example.animeapp.presentation.theme.AnimeAppTheme
import com.example.animeapp.presentation.theme.LightGreen
import com.example.animeapp.domain.viewModel.viewModelFactory.MyViewModelFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


//@HiltAndroidApp
class MainActivity : ComponentActivity() {


    private lateinit var navController: NavHostController
    private var modifier: Modifier = Modifier

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        noTitleBarAndSplashScreenActivator()


        setContent {
            changeStatusBarColor()
            val myViewModelFactory = MyViewModelFactory(MalApiService.api)
            val viewModelProvider = ViewModelProvider(this, myViewModelFactory)

            AnimeAppTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = modifier.fillMaxSize(),
//                    color = MainBackgroundColor


                ) {

                    navController = rememberNavController()

                    TokoAppActivator(
                        navController = navController,
                        viewModelProvider = viewModelProvider,
                        context = this,
                        modifier = modifier
                    )


//                    System.setProperty("webdriver.ie.driver", "IEDriverServer.exe")
//                    val driver: WebDriver = ChromeDriver()
//                    val link = driver.get("https://myanimelist.net/news")
//                    driver.get("chrome://settings/clearBrowserData")
//                    Log.d("MAIN-SELENIUM", link.toString())


                }
            }
        }
    }

    //    override fun onDestroy() {
//        super.onDestroy()
//        navController.popBackStack()
//        println("STACK IS POPED")
//    }
    private fun noTitleBarAndSplashScreenActivator() {
        window.navigationBarColor = LightGreen.toArgb()
        window.statusBarColor = LightGreen.toArgb()
        requestWindowFeature(Window.FEATURE_NO_TITLE) // Undo topBar
        installSplashScreen() // Custom Splash Screen
    }

    @Composable
    private fun changeStatusBarColor() {
        SideEffect {
            MainScope().launch {
                window.statusBarColor = LightGreen.toArgb()
            }
        }
    }
}







