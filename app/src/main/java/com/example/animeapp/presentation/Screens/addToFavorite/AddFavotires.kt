package com.example.animeapp.presentation.Screens.addToFavorite

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.animeapp.dao.AnimeItem
import com.example.animeapp.dao.MainDb
import com.example.animeapp.presentation.Screens.homeScreen.checkIdInDataBase
import com.example.animeapp.presentation.theme.LightYellow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddFavorites(
    mal_id: Int,
    anime: String,
    score: String,
    scoredBy: String,
    animeImage: String,
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }
    val items = mutableListOf("Planned", "Watching", "Watched", "Dropped")
    val dao = MainDb.getDb(context).getDao()
    if (checkIdInDataBase(
            dao = dao,
            id = mal_id
        ).collectAsStateWithLifecycle(initialValue = false).value
    ) {
        items.add(4, "Delete")
    }

    // Keep track of the selected item
    var selectedItem by remember { mutableStateOf("") }

    // Fetch data when the button is clicked on a specific item
    LaunchedEffect(selectedItem) {
        if (selectedItem.isNotEmpty()) {
            coroutineScope.launch(Dispatchers.IO) {
//                val dao = MainDb.getDb(context).getDao()
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
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
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
            modifier = Modifier.background(LightYellow)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        selectedItem = item
                        expanded = false
                    },
                    text = { Text(text = item) }
                )
            }
        }
    }


}