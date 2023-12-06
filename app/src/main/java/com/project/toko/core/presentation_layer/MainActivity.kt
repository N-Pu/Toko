package com.project.toko.core.presentation_layer


import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.toko.core.dao.MainDb
import com.project.toko.core.di.Application
import com.project.toko.core.repository.MalApiService
import com.project.toko.core.presentation_layer.appConstraction.AppActivator
import com.project.toko.core.presentation_layer.theme.TokoTheme
import com.project.toko.core.viewModel.viewModelFactory.MyViewModelFactory
import javax.inject.Inject


class MainActivity : ComponentActivity() {


    lateinit var navController: NavHostController

    @Inject
    lateinit var mainDb: MainDb

    @Inject
    lateinit var modifier: Modifier

    @Inject
    lateinit var malApi: MalApiService


    override fun onCreate(savedInstanceState: Bundle?) {

//        enableEdgeToEdge(
//            statusBarStyle = SystemBarStyle.light(
//                android.graphics.Color.TRANSPARENT,
//                android.graphics.Color.TRANSPARENT
//            ),
//            navigationBarStyle = SystemBarStyle.light(
//                android.graphics.Color.TRANSPARENT,
//                android.graphics.Color.TRANSPARENT
//            )
//        )
        super.onCreate(savedInstanceState)
//        navigationBarColorChanger()

        requestWindowFeature(Window.FEATURE_NO_TITLE)


//        WindowCompat.setDecorFitsSystemWindows(window,false)
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
        setContent {
//            HideStatusBar()
            navController = rememberNavController()

            TokoTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = modifier.fillMaxSize(),
                ) {
                    AppActivator(
                        navController = navController,
                        viewModelProvider = viewModelProvider,
                        modifier = modifier,
                        componentActivity = this,
                        mainDb = databaseComponent.provideDao()
                    )
                }
            }
        }
    }


//    private fun navigationBarColorChanger() {
//        window.navigationBarColor = LightGreen.copy(alpha = 1f).toArgb()
//    }

//    @Composable
//    private fun HideStatusBar() {
//        SideEffect {
//            MainScope().launch {
//                window.statusBarColor = Color.Transparent.toArgb()
//            }
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
//
//            window.decorView.doOnLayout {
//                WindowCompat.setDecorFitsSystemWindows(window, false)
//                windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
//            }
//        } else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//            )
//        }
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        dataCache.clear()
//        DataCacheSingleton.dataCache.clear()
//    }

}

