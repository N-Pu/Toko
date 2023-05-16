package com.example.animeapp.presentation.Screens.favoritesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.animeapp.dao.Dao
import com.example.animeapp.dao.MainDb

@Composable
fun Fav(navController: NavController) {

    AnimeListScreen()

}


@Preview(showSystemUi = true)
@Composable
fun PreviewFavoriteScreen() {
//AnimeScreen()

    AnimeListScreen()


}


enum class AnimeListType {
    WATCHING,
    PLANNED,
    WATCHED,
    DROPPED
}

@Composable
fun AnimeListScreen() {
    var selectedListType by remember { mutableStateOf(AnimeListType.WATCHING) }
    val dao = MainDb.getDb(LocalContext.current).getDao()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            Modifier
                .weight(1f / 9)
                .background(Color.Red)
        ) {
            TextButton(
                onClick = { selectedListType = AnimeListType.WATCHING },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedListType == AnimeListType.WATCHING) Color.Green else Color.LightGray
                )
            ) {
                Text("WATCHING")
            }

            Button(
                onClick = { selectedListType = AnimeListType.PLANNED },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedListType == AnimeListType.PLANNED) Color.Green else Color.LightGray
                )
            ) {
                Text("PLANNED")
            }

            Button(
                onClick = { selectedListType = AnimeListType.WATCHED },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedListType == AnimeListType.WATCHED) Color.Green else Color.LightGray
                )
            ) {
                Text("WATCHED")
            }

            Button(
                onClick = { selectedListType = AnimeListType.DROPPED },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedListType == AnimeListType.DROPPED) Color.Green else Color.LightGray
                )
            ) {
                Text("DROPPED")
            }
        }

        // Разный список аниме в зависимости от выбранного типа
        when (selectedListType) {
            AnimeListType.WATCHING -> AnimeListWatching(dao)
            AnimeListType.PLANNED -> AnimeListPlanned(dao)
            AnimeListType.WATCHED -> AnimeListWatched(dao)
            AnimeListType.DROPPED -> AnimeListDropped(dao)
        }
    }
}


@Composable
fun AnimeListWatching(dao: Dao) {
    val animeListState by dao.getWatchingAnime().collectAsState(initial = emptyList())

    // Отображение списка аниме
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9.5f / 10f)
    ) {
        Text("Список аниме, которые я смотрю", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(16.dp))

        animeListState.forEach { animeItem ->
            Text(animeItem.anime, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun AnimeListPlanned(dao: Dao) {
    val animeListState by dao.getPlannedAnime().collectAsState(initial = emptyList())

    // Отображение списка аниме
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9.5f / 10f)
    ) {
        Text("Список аниме, которые я планирую посмотреть", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(16.dp))

        animeListState.forEach { animeItem ->
            Text(animeItem.anime, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun AnimeListWatched(dao: Dao) {
    val animeListState by dao.getWatchedAnime().collectAsState(initial = emptyList())

    // Отображение списка аниме
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9.5f / 10f)
    ) {
        Text("Список аниме, которые я смотрел", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(16.dp))

        animeListState.forEach { animeItem ->
            Text(animeItem.anime, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun AnimeListDropped(dao: Dao) {
    val animeListState by dao.getDroppedAnime().collectAsState(initial = emptyList())

    // Отображение списка аниме
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9.5f / 10f)
    ) {
        Text("Список аниме, которые я бросил", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(16.dp))

        animeListState.forEach { animeItem ->
            Text(animeItem.anime, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

