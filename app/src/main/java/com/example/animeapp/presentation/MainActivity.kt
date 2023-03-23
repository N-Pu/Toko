package com.example.animeapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.animeapp.presentation.custom.CustomItem
import com.example.animeapp.presentation.custom.MainViewModel
import com.example.animeapp.ui.theme.AnimeAppTheme
import com.example.animeapp.ui.domain.models.searchModel.Data
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @OptIn(DelicateCoroutinesApi::class)
//    @SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel.loadList()
                    val data = viewModel.dataListResponse.observeAsState()
                    if (data.value != null) {
                        MakeListOfAnime(data = data.value!!.toList())
                    }

                }
            }
        }
    }
}



@Composable
fun MakeListOfAnime(data: List<Data>) {

    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        itemsIndexed(data) { _, item ->
            CustomItem(data = item)
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    TimerScreen()
//}



