package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.project.toko.core.dao.AnimeItem
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.core.viewModel.daoViewModel.DaoViewModel
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.homeScreen.presentation_layer.homeScreen.formatScore
import com.project.toko.homeScreen.presentation_layer.homeScreen.formatScoredBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun AddToFavorites(
    viewModelProvider: ViewModelProvider,
    modifier: Modifier
) {


    val items = mutableListOf("Planned", "Watching", "Watched", "Dropped")
    val detailScreenViewModel = viewModelProvider[DetailScreenViewModel::class.java]
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    val detailScreenState by viewModelProvider[DetailScreenViewModel::class.java]
        .animeDetails.collectAsStateWithLifecycle()
    val containsInDao = daoViewModel.containsInDataBase(id = detailScreenState?.mal_id ?: 0
    ).collectAsStateWithLifecycle(initialValue = false).value
    if (containsInDao
    ) {
        items.add(4, "Delete")
    }
    var selectedItem by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = modifier
                .height(70.dp)
                .width(70.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    detailScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
                        expanded = true
                    }

                },
                colors = IconButtonDefaults.iconButtonColors(containerColor = LightGreen),
                modifier = modifier
                    .height(50.dp)
                    .width(50.dp)
            ) {
                if (expanded) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Localized description"
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Localized description"
                    )
                }
            }
        }
//    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = modifier
            .background(LightGreen),
        offset = DpOffset(x = (100).dp, y = (0).dp)
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    detailScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
                        selectedItem = item
                        if (selectedItem == "Delete") {
                            detailScreenState?.let { data ->
                                daoViewModel.removeFromDataBase(data.mal_id)
                            }
                        } else {
                            detailScreenState?.let { data ->
                                daoViewModel.addToCategory(
                                    AnimeItem(
                                        data.mal_id,
                                        anime = data.title,
                                        score = formatScore(data.score),
                                        scored_by = formatScoredBy(data.scored_by),
                                        animeImage = data.images.jpg.large_image_url,
                                        category = selectedItem
                                    )
                                )
                            }
                        }
                        // on touched - dropDownMenu cancels
                        expanded = false
                    }
                },
                colors = MenuDefaults.itemColors(textColor = Color.White),
                text = {
                    Text(text = item)
                })
        }
    }

}

