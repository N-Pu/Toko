package com.example.animeapp.presentation.firstScreen


import HomeScreenViewModel
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.animeapp.presentation.animations.LoadingAnimation



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenSearchPanel(navController: NavHostController) {
    val viewModel =
        viewModel<HomeScreenViewModel>()

    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val animeList by viewModel.animeList.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Search anime...") },
            enabled = true,
            shape = RoundedCornerShape(36.dp),
            maxLines = 1
        )

        if (isSearching.not()) {
            GridAdder(listData = animeList, navController = navController)
        } else {
            LoadingAnimation()
        }

    }
}

