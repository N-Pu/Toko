package com.example.animeapp.presentation.screens.detailScreen.sideContent.staffList

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.presentation.navigation.DetailOnStaff
import com.example.animeapp.presentation.navigation.Screen

@Composable
fun DisplayStaff(
    staffList: List<com.example.animeapp.domain.models.staffModel.Data>,
    navController: NavController
) {
    ListEditor(listData = staffList, navController = navController)
}


@Composable
fun ListEditor(
    listData: List<com.example.animeapp.domain.models.staffModel.Data>,
    navController: NavController
) {
//    val trimmedStaff = isMoreThenTen(listData)
    Text(text = "Staff", textDecoration = TextDecoration.Underline)
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listData.take(10).forEach { data ->
            val personPainter =
                rememberAsyncImagePainter(model = data.person.images.jpg.image_url)

            StaffComponentsCard(
                data = data,
                personPainter = personPainter,
                navController = navController
            )
            Spacer(modifier = Modifier.size(20.dp))

        }

    }
    Box(modifier = Modifier.fillMaxWidth(0.9f)) {
        Text(
            text = "More Staff",
            textAlign = TextAlign.Left,
            color = Color.Blue,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.align(Alignment.BottomEnd).clickable {

                navController.navigate(DetailOnStaff.value) {
                    // Удаление экрана ShowWholeStaff и всех экранов выше его в стеке обратной навигации
                    popUpTo(Screen.Detail.route) {
                        inclusive = true
                    }


                }


            })
    }
}


@Composable
fun StaffComponentsCard(
    data: com.example.animeapp.domain.models.staffModel.Data,
    personPainter: AsyncImagePainter,
    navController: NavController
) {
    Card(modifier = Modifier
        .size(123.dp, 150.dp)
        .clickable {
            navController.navigate(route = "detail_on_staff/${data.person.mal_id}") {

                popUpTo(Screen.Detail.route) {
                    inclusive = true
                }

            }
            Log.d("staff id ->", data.person.mal_id.toString())
        }) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {

            Image(
                painter = personPainter,
                contentDescription = "Voice actor : ${data.person.name}",
                modifier = Modifier.aspectRatio(9f / 11f),
                contentScale = ContentScale.Crop
            )
            Text(
                text = data.person.name,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 8.dp, bottom = 8.dp)
                    .shadow(4.dp, shape = MaterialTheme.shapes.small),
                color = Color.White
            )
            Spacer(modifier = Modifier.size(20.dp))
            data.positions.forEach {
                Text(
                    text = it,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(end = 8.dp, bottom = 8.dp)
                        .shadow(4.dp, shape = MaterialTheme.shapes.small),
                    color = Color.White
                )
            }

        }
    }
}