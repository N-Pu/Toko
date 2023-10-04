package com.project.toko.core.presentation_layer


import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.toko.core.dao.Dao
import com.project.toko.core.di.Application
import com.project.toko.core.model.cache.DataCache
import com.project.toko.core.repository.MalApiService
import com.project.toko.core.presentation_layer.appConstraction.TokoAppActivator
import com.project.toko.core.presentation_layer.theme.TokoTheme
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.core.viewModel.viewModelFactory.MyViewModelFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject


//@HiltAndroidApp
class MainActivity : ComponentActivity() {


    lateinit var navController: NavHostController

    @Inject
    lateinit var dao: Dao

    @Inject
    lateinit var modifier: Modifier

    @Inject
    lateinit var malApi: MalApiService

    @Inject
    lateinit var dataCache: DataCache
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        navigationBarColorChanger()

        val modifierComponent = (application as Application).modifierComponent
        val databaseComponent = (application as Application).daoComponent
        val malApiComponent = (application as Application).malApiComponent
        val dataCacheComponent = (application as Application).dataCacheComponent
        modifier = modifierComponent.providesModifier()
        dao = databaseComponent.provideDao()
        malApi = malApiComponent.provideMalApiService()
        dataCache = dataCacheComponent.provideDataCache()

        val myViewModelFactory =
            MyViewModelFactory(malApiRepository = malApi, dao = dao, dataCache = dataCache)
        val viewModelProvider = ViewModelProvider(this, myViewModelFactory)
        setContent {
            HideStatusBar()
            navController = rememberNavController()

            TokoTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = modifier.fillMaxSize(),
                ) {
                    TokoAppActivator(
                        navController = navController,
                        viewModelProvider = viewModelProvider,
                        modifier = modifier
                    )
                }
            }
        }
    }

    private fun navigationBarColorChanger() {
        window.navigationBarColor = LightGreen.copy(alpha = 1f).toArgb()
    }

    @Composable
    private fun HideStatusBar() {
        SideEffect {
            MainScope().launch {
                window.statusBarColor = Color.Transparent.toArgb()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

            window.decorView.doOnLayout {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dataCache.clear()
//        DataCacheSingleton.dataCache.clear()
    }

}