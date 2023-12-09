package com.project.toko.core.presentation_layer


import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.project.toko.core.dao.MainDb
import com.project.toko.core.di.Application
import com.project.toko.core.repository.MalApiService
import com.project.toko.core.presentation_layer.appConstraction.AppActivator
import com.project.toko.core.presentation_layer.theme.TokoTheme
import com.project.toko.core.settings.SaveDarkMode
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

    private lateinit var darkTheme: SaveDarkMode
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        setContent {
            navController = rememberNavController()

            TokoTheme(darkTheme.isDarkThemeActive.value) {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = modifier.fillMaxSize(),
                ) {
                    AppActivator(
                        navController = navController,
                        viewModelProvider = viewModelProvider,
                        modifier = modifier,
                        componentActivity = this,
                        mainDb = databaseComponent.provideDao(),
                        onThemeChange = {
                            darkTheme.isDarkThemeActive.value = !darkTheme.isDarkThemeActive.value
                            darkTheme.saveData(darkTheme.isDarkThemeActive.value)
                        },
                        isInDarkTheme = darkTheme.isDarkThemeActive.value,
                        svgImageLoader = svgImageLoader
                    )
                }
            }
        }
    }
}

