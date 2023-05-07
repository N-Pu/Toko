package com.example.animeapp.presentation.detailScreen.castList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.R
import com.example.animeapp.domain.staffModel.Person
import com.example.animeapp.presentation.navigation.Screen
import com.example.animeapp.viewModel.StaffInDetailScreenViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShowWholeStaff(navController: NavController, viewModelProvider: ViewModelProvider) {


    val staffState =
        viewModelProvider[StaffInDetailScreenViewModel::class.java].staffList.collectAsStateWithLifecycle()

//    Surface(modifier = Modifier.fillMaxSize()) {


    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 265.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.size(20.dp)) }
        items(staffState.value) { data ->
            SingleStaffMember(person = data.person, positions = data.positions, navController = navController)
        }
        item { Spacer(modifier = Modifier.size(50.dp)) }

    }

}
//}


@Composable
fun SingleStaffMember(person: Person, positions: List<String>, navController: NavController) {
//    character.name
//    character.images.jpg
//    character.mal_id
//    character.url

    var isVisible by remember {
        mutableStateOf(false)
    }


    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
//            initialAlpha = 0.0f,
            animationSpec = TweenSpec(durationMillis = 500)
        ),
        exit = slideOutHorizontally(
            animationSpec = TweenSpec(durationMillis = 500)
        )
    ) {

        val painter = rememberAsyncImagePainter(model = person.images.jpg.image_url)
        Card(modifier = Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate(route = "detail_on_staff/${person.mal_id}") {

                    popUpTo(Screen.Detail.route) {
                        inclusive = true
                    }

                }
            }) {
            Row {
                Column {
                    Image(
                        painter = painter,
                        contentDescription = person.name,
                        modifier = Modifier.size(123.dp, 150.dp)
                    )
                    Text(
                        text = person.name,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(100.dp)
                    )
                }
                Column {
                    positions.forEach { position ->
                        Text(text = position)
                    }
                }
            }

        }

    }



    LaunchedEffect(person) {
        isVisible = true
    }

    Spacer(modifier = Modifier.size(10.dp))

}


@Preview(showSystemUi = true)
@Composable
fun PreviewWholeStaff() {
    val imageResource = painterResource(R.drawable.kurisu)
    val listOfStrings: List<String> = listOf(
        "строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1",
        "строка 2",
        "строка 3"
    )



    Surface(modifier = Modifier.fillMaxSize()) {

        LazyColumn {
            item { Spacer(modifier = Modifier.size(20.dp)) }
            item {
                Card {
                    Row {
                        Column {
                            Image(painter = imageResource, contentDescription = "")
                            Text(
                                text = "Kurisu",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                        Column {
                            listOfStrings.forEach {
                                Text(text = it)
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.size(10.dp))
            }
            item {
                Card {
                    Row {
                        Column {
                            Image(painter = imageResource, contentDescription = "")
                            Text(
                                text = "Kurisu",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                        Column {
                            listOfStrings.forEach {
                                Text(text = it)
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.size(10.dp))
            }
            item {
                Card {
                    Row {
                        Column {
                            Image(painter = imageResource, contentDescription = "")
                            Text(
                                text = "Kurisu",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                        Column {
                            listOfStrings.forEach {
                                Text(text = it)
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.size(10.dp))
            }
            item {
                Card {
                    Row {
                        Column {
                            Image(painter = imageResource, contentDescription = "")
                            Text(
                                text = "Kurisu",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                        Column {
                            listOfStrings.forEach {
                                Text(text = it)
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.size(10.dp))
            }
            item {
                Card {
                    Row {
                        Column {
                            Image(painter = imageResource, contentDescription = "")
                            Text(
                                text = "Kurisu",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                        Column {
                            listOfStrings.forEach {
                                Text(text = it)
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.size(10.dp))
            }
            item {
                Card {
                    Row {
                        Column {
                            Image(painter = imageResource, contentDescription = "")
                            Text(
                                text = "Kurisu",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                        Column {
                            listOfStrings.forEach {
                                Text(text = it)
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.size(10.dp))
            }
            item {
                Card {
                    Row {
                        Column {
                            Image(painter = imageResource, contentDescription = "")
                            Text(
                                text = "Kurisu",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                        Column {
                            listOfStrings.forEach {
                                Text(text = it)
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.size(10.dp))
            }
            item {
                Card {
                    Row {
                        Column {
                            Image(painter = imageResource, contentDescription = "")
                            Text(
                                text = "Kurisu",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                        Column {
                            listOfStrings.forEach {
                                Text(text = it)
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.size(10.dp))
            }
            item {
                Card {
                    Row {
                        Column {
                            Image(painter = imageResource, contentDescription = "")
                            Text(
                                text = "Kurisu",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                        Column {
                            listOfStrings.forEach {
                                Text(text = it)
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.size(10.dp))
            }

        }
    }
}
