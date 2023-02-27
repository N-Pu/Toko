package com.example.animeapp.presentation

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.example.animeapp.presentation.custom.CustomItem
import com.example.animeapp.ui.theme.AnimeAppTheme
import com.example.animeapp.ui.theme.domain.models.AnimeSearchModel
import com.example.animeapp.ui.theme.domain.models.Data
import com.example.animeapp.ui.theme.domain.repository.ApiService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {


    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val scope = rememberCoroutineScope()
                    var job: Job? by remember {
                        mutableStateOf(null)
                    }

                    job = scope.launch {
                        val response = ApiService.api.getAnimeModel()
                        if (response.isSuccessful) {
//                            response.body()?.let { MakeListOfAnime(data = it.data) }
                            Log.d("MAIN-ACTIVITY", "onResponse activated")
                            response.body()?.data?.forEach {
                                Log.d(
                                    "->",
                                    "[" + it.title + "| " + "EPISODES:" + it.episodes + "  |" + it.rating + "]"
                                )
                            }
                        }

                    }
//                    MakeListOfAnime(job)

                }
            }
        }
    }
}

@Composable
 fun getResponse(): Response<AnimeSearchModel> {
    TODO()

}

@Composable
fun MakeListOfAnime(data: List<Data>) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(data) { item ->
            CustomItem(data = item)
        }
    }
}

@Composable
fun TimerScreen() {
    val scope = rememberCoroutineScope()
    Column {
        Button(onClick = {
            println("Timer started")
            scope.launch {
                try {
                    println("Timer ended")

                } catch (ex: Exception) {
                    println("Timer cancelled")
                }
            }
        }) {
            Text("Start Timer")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TimerScreen()
}



