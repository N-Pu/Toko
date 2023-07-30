package com.project.toko.presentation.screens.detailScreen.mainPage.customVisuals

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.toko.domain.models.newAnimeSearchModel.Genre
import com.project.toko.presentation.theme.LightGreen

@Composable
private fun ColorBox(
    text: String,
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .background(LightGreen, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(4.dp)
        )
    }
}

@Composable
fun DisplayCustomGenreBoxes(genres: List<Genre>, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.Center
    ) {
        genres.forEachIndexed { index, genre ->
            if (index != 0) {
                Spacer(modifier = modifier.width(8.dp))
            }
            ColorBox(
                text = genre.name,
                modifier
            )
        }
    }
}


// Пример использования
//@Preview
//@Composable
//fun ExampleUsage() {
//    val genres = listOf(
//        Genre(name = "Action", mal_id = 1, url = "d", type = "d"),
//        Genre(name = "Action", mal_id = 1, url = "d", type = "d"),
//        Genre(name = "Action", mal_id = 1, url = "d", type = "d"),
//        Genre(name = "Action", mal_id = 1, url = "d", type = "d"),
//        Genre(name = "Action", mal_id = 1, url = "d", type = "d"),
//
//
//        )
//
////    DisplayCustomGenreBoxes(genres)
//}