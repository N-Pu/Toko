package com.example.animeapp.presentation.secondScreen.StaffList

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.domain.staffModel.Data
import com.example.animeapp.viewModel.StaffViewModel

@Composable
fun DisplayStaffList(mal_id: Int) {
    val viewModel = viewModel<StaffViewModel>()
    viewModel.addStaffFromId(mal_id)
    val staffList by viewModel.staffList.collectAsStateWithLifecycle()
    LazyListEditor(listData = staffList)
}


@Composable
fun LazyListEditor(listData: List<Data>) {
    Text(text = "Staff", textDecoration = TextDecoration.Underline)
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(listData) { _, data ->
            val personPainter =
                rememberAsyncImagePainter(model = data.person.images.jpg.image_url)

            StaffComponentsCard(data = data, personPainter = personPainter)
            Spacer(modifier = Modifier.size(20.dp))
        }

    }
}


@Composable
fun StaffComponentsCard(data: Data, personPainter: AsyncImagePainter) {
    Card(modifier = Modifier.size(123.dp, 150.dp)) {
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