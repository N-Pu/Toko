package com.example.animeapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.animeapp.presentation.searchPanel.MainScreenSearchPanel
import com.example.animeapp.viewModel.MainViewModel
import com.example.animeapp.presentation.theme.AnimeAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenSearchPanel()

//                    viewModel.inputNameOfSearch()

//                    val data = viewModel.dataListResponse.observeAsState()
//                    if (data.value != null) {
//                        MakeListOfAnime(data = data.value!!.toList())
//                    }

                }
            }
        }
    }
}








//@Composable
//fun MakeListOfAnime(data: List<Data>) {
//
//    LazyColumn(modifier = Modifier.fillMaxHeight()) {
//        itemsIndexed(data) { _, item ->
//            CustomItem(data = item)
//        }
//    }
//}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    TimerScreen()
//}



