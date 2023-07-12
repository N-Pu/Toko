package com.example.animeapp.presentation.addToFavorite

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.animeapp.dao.AnimeItem
import com.example.animeapp.dao.MainDb
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
    context: Context,
    modifier: Modifier,
    viewModel: ViewModel
) {
    val items = mutableListOf("Planned", "Watching", "Watched", "Dropped")
    var expanded by remember { mutableStateOf(false) }
    val dao = MainDb.getDb(context).getDao()
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
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.inversePrimary
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(LightGreen)
                .align(Alignment.BottomEnd)
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



                }, text = { Text(text = item) })
            }
        }
    }
}
