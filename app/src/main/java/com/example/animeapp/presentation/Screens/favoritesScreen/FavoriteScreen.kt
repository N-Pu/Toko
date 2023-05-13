package com.example.animeapp.presentation.Screens.favoritesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp

@Composable
fun Fav(navController: NavController) {
//    if (navController.previousBackStackEntry?.destination?.route == DetailOnCast.value){
//        navController.popBackStack(DetailOnCast.value, inclusive = true)
//    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {

        Text(text = "FAV")
    }
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

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Blue)) {
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
            AnimeListType.WATCHING -> AnimeListWatching()
            AnimeListType.PLANNED -> AnimeListPlanned()
            AnimeListType.WATCHED -> AnimeListWatched()
            AnimeListType.DROPPED -> AnimeListDropped()
        }
    }
}

@Composable
fun AnimeListWatching() {
    // Список аниме для типа "Смотрю"
    val animeList = listOf("Аниме 1", "Аниме 2", "Аниме 3", "Аниме 3", "Аниме 3")

    // Отображение списка аниме
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9.5f / 10f)
    ) {
        Text("Список аниме, которые я смотрю", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(16.dp))

        for (anime in animeList) {
            Text(anime, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AnimeListPlanned() {
    val animeList = listOf("Аниме 1", "Аниме 3")

    // Отображение списка аниме
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9.5f / 10f)
    ) {
        Text("Список аниме, которые я смотрю", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(16.dp))

        for (anime in animeList) {
            Text(anime, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AnimeListWatched() {
    val animeList = listOf(
        "Аниме 1",
        "Аниме 2",
        "Аниме 3",
        "Аниме 3",
        "Аниме 3",
        "Аниме 3",
        "Аниме 3",
        "Аниме 3",
        "Аниме 3",
        "Аниме 3",
        "Аниме 3",
        "Аниме 3",
        "Аниме 3",
        "Аниме 3",
        "Аниме 3",
        "Аниме 3"
    )

    // Отображение списка аниме
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9.5f / 10f)
    ) {
        Text("Список аниме, которые я смотрю", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(16.dp))

        for (anime in animeList) {
            Text(anime, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AnimeListDropped() {
    val animeList = listOf("Аниме 1")

    // Отображение списка аниме
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9.5f / 10f)
    ) {
        Text("Список аниме, которые я смотрю", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(16.dp))

        for (anime in animeList) {
            Text(anime, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


//@Composable
//fun AnimeScreen() {
//    Column(modifier = Modifier.fillMaxSize()) {
//        // Row с кнопками
//        Row(
//            modifier = Modifier
////                .height(IntrinsicSize.Max).
//                . fillMaxWidth()
////                .weight(1f / 9f)
//        ) {
//            ButtonArea(text = "Смотрю")
//            ButtonArea(text = "Запланировано")
//            ButtonArea(text = "Просмотрено")
//            ButtonArea(text = "Брошено")
//        }
//
//        // Контент для каждой кнопки
//        }
//    }
//
//
//@Composable
//fun ButtonArea(text: String) {
//    Box(
//        modifier = Modifier
//            .background(Color.Red)
//            .clickable(onClick = {
//
//            }),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(text)
//    }
//}
