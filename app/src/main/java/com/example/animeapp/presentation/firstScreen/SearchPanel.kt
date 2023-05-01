package com.example.animeapp.presentation.firstScreen


import HomeScreenViewModel
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.animeapp.presentation.animations.LoadingAnimation
import com.example.animeapp.viewModel.IdViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    searchViewModel: HomeScreenViewModel,
    idViewModel: IdViewModel,
//    savedAnimeViewModel: SavedAnimeViewModel
) {


    val searchText by searchViewModel.searchText.collectAsStateWithLifecycle()
    val animeList by searchViewModel.animeList.collectAsStateWithLifecycle()
    val isSearching by searchViewModel.isSearching.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = searchViewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
//                Text(text = "Search anime...")
                    Icon(Icons.Filled.Search, "Search Icon")
                    },
            enabled = true,
            shape = RoundedCornerShape(36.dp),
           singleLine = true,
            placeholder = {
                Text(text = "Searching anime...")
            },
//            leadingIcon = {
//                Icon(Icons.Filled.Person, "Search Icon")
//            }

        )

        if (isSearching.not()) {
            GridAdder(
                searchViewModel = searchViewModel,
                navController = navController,
                idViewModel = idViewModel,
                listData = animeList
            )
        } else {
            LoadingAnimation()
        }

    }
}

