package com.example.animeapp.presentation.Screens.homeScreen


import HomeScreenViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.animeapp.presentation.animations.LoadingAnimation
import com.example.animeapp.domain.viewModel.IdViewModel
import kotlinx.coroutines.launch


//@Composable
//fun MainScreen(
//    navController: NavHostController,
//    homeScreenViewModel: HomeScreenViewModel,
//    idViewModel: IdViewModel,
////    savedAnimeViewModel: SavedAnimeViewModel
//) {
//
//
//    val searchText by homeScreenViewModel.searchText.collectAsStateWithLifecycle()
//    val animeList by homeScreenViewModel.animeList.collectAsStateWithLifecycle()
//    val isSearching by homeScreenViewModel.isSearching.collectAsStateWithLifecycle()
//
//
//    Column(
//        modifier = Modifier
//            .padding(16.dp)
//    ) {
//        OutlinedTextField(
//            value = searchText,
//            onValueChange = homeScreenViewModel::onSearchTextChange,
//            modifier = Modifier.fillMaxWidth(),
//            label = {
////                Text(text = "Search anime...")
//                Icon(Icons.Filled.Search, "Search Icon")
//            },
//            enabled = true,
//            shape = RoundedCornerShape(36.dp),
//            singleLine = true,
//            placeholder = {
//                Text(text = "Searching anime...")
//            },
////            leadingIcon = {
////                Icon(Icons.Filled.Person, "Search Icon")
////            }
//
//        )
//
//        if (isSearching.not()) {
//            GridAdder(
//                searchViewModel = homeScreenViewModel,
//                navController = navController,
//                idViewModel = idViewModel,
//                listData = animeList
//            )
//        } else {
//            LoadingAnimation()
//        }
//
//    }
//}

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    val searchText by remember { viewModelProvider[HomeScreenViewModel::class.java].searchText }.collectAsState()
//    val animeList by remember { viewModelProvider[HomeScreenViewModel::class.java].animeList }.collectAsState()
    val isSearching by remember { viewModelProvider[HomeScreenViewModel::class.java].isSearching }.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {

          OutlinedTextField(
                value = searchText,
                onValueChange = viewModelProvider[HomeScreenViewModel::class.java]::onSearchTextChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Icon(Icons.Filled.Search, "Search Icon")
                },

                shape = RoundedCornerShape(36.dp),
                singleLine = true,
                placeholder = {
                    Text(text = "Searching anime...")
                },
            )

            if (isSearching) {
                LoadingAnimation()
            }


        if (!isSearching) {
            GridAdder(

                navController = navController,
                viewModelProvider = viewModelProvider

            )
        }

        LaunchedEffect(key1 = searchText) {
            coroutineScope.launch {
                viewModelProvider[HomeScreenViewModel::class.java].onSearchTextChange(searchText)
            }
        }
    }
}

