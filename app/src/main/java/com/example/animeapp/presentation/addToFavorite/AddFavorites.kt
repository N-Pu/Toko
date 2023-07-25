package com.example.animeapp.presentation.addToFavorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.animeapp.dao.AnimeItem
import com.example.animeapp.dao.Dao
import com.example.animeapp.presentation.screens.homeScreen.checkIdInDataBase
import com.example.animeapp.presentation.theme.LightGreen
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
    viewModel: ViewModel,
    dao: Dao
) {
    val items = mutableListOf("Planned", "Watching", "Watched", "Dropped")
    var expanded by remember { mutableStateOf(false) }
    if (checkIdInDataBase(
            dao = dao, id = mal_id
        ).collectAsStateWithLifecycle(initialValue = false).value
    ) {
        items.add(4, "Delete")
    }

    // Keep track of the selected item
    var selectedItem by remember { mutableStateOf("") }

    // Fetch data when the button is clicked on a specific item
    BoxWithConstraints(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = { expanded = true },
            ) {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = null,
                    tint = LightGreen
                )
            }
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
                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                        selectedItem = item
                        if (selectedItem == "Delete") {
                            dao.removeFromDataBase(mal_id)
                        } else {
                            dao.addToCategory(
                                AnimeItem(
                                    mal_id,
                                    anime = anime,
                                    score = score,
                                    scored_by = scoredBy,
                                    animeImage = animeImage,
                                    category = selectedItem
                                )
                            )
                        }
                        // on touched - dropDownMenu cancels
                        expanded = false
                    }


                },
                    colors = MenuDefaults.itemColors(textColor = Color.White),
                    text = { Text(text = item) })
            }
        }
    }
}
