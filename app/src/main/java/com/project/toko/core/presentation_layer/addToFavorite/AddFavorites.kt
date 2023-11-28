package com.project.toko.core.presentation_layer.addToFavorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.project.toko.R
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.daoScreen.daoViewModel.DaoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// Function that starts when you click on
// button "+" to add anime in 4 different
// categories and shows dropDownMenu that
// contains those categories (watching,
// dropped, planned, watched). User can
// tap on them and anime that was selected
// will be send to data base and placed in

@Composable
fun AddFavorites(
    mal_id: Int,
    anime: String,
    score: String,
    scoredBy: String,
    animeImage: String,
    modifier: Modifier,
    viewModelProvider: ViewModelProvider,
    rating: String,
    status: String,
    secondName: String?,
    airedFrom: String?,
    type: String,
    imageLoader: ImageLoader

) {

    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    val items = mutableListOf("Planned", "Watching", "Watched", "Dropped")
    var expanded by remember { mutableStateOf(false) }

    val getAnimeCategory by daoViewModel.getCategoryForId(mal_id).collectAsStateWithLifecycle(
        initialValue = null
    )

    if (getAnimeCategory != null
    ) {
        items.add(4, "Delete")
    }

    // Fetch data when the button is clicked on a specific item
    BoxWithConstraints(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Image(
                modifier = modifier
                    .size(38.dp)
                    .clickable { expanded = true },
                painter = rememberAsyncImagePainter(
                    model = R.drawable.addpluscircle,
                    imageLoader = imageLoader
                ),
                contentDescription = "Add circle"
            )

        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(LightGreen)
                .align(Alignment.BottomEnd),
            offset = DpOffset((40).dp, (-30).dp)
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                        if (item == "Delete") {
                            daoViewModel.removeFromDataBase(AnimeItem(
                                id = mal_id,
                                animeName = anime,
                                score = score,
                                scored_by = scoredBy,
                                animeImage = animeImage,
                                category = getAnimeCategory,
                                status = status,
                                secondName = secondName ?: "N/A",
                                rating = rating,
                                airedFrom = airedFrom ?: "N/A",
                                type = type
                            ))
                        } else {
                            daoViewModel.addToCategory(
                                AnimeItem(
                                    id = mal_id,
                                    animeName = anime,
                                    score = score,
                                    scored_by = scoredBy,
                                    animeImage = animeImage,
                                    category = item,
                                    status = status,
                                    secondName = secondName ?: "N/A",
                                    rating = rating,
                                    airedFrom = airedFrom ?: "N/A",
                                    type = type
                                )
                            )
                        }
                        // on touched - dropDownMenu cancels
                        expanded = false
                    }

//                    daoViewModel.viewModelScope.launch(Dispatchers.Main) {
//                        if (item == "Delete") {
//                            Toast.makeText(
//                                context,
//                                "$anime is removed from database!",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
//                            Toast.makeText(
//                                context,
//                                "$anime is in $item category!",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
                },
                    colors = MenuDefaults.itemColors(textColor = Color.White),
                    text = { Text(text = item) })
            }
        }
    }


}
