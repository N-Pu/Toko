package com.example.animeapp.viewModel


import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.repository.ApiService.Companion.api
import com.example.animeapp.domain.searchModel.Data
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class HomeScreenViewModel : ViewModel() {


    private val animeRepository = api

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _animeList = MutableStateFlow<List<Data>>(emptyList())
    val animeList = _animeList.asStateFlow()
    fun onSearchTextChange(text: String) {
        _searchText.value = text
        viewModelScope.launch {
            try {
                if (text.isNotBlank()) {

                    _isSearching.update {
                        true
                    }

                    _animeList.value = animeRepository
                        .getAnimeSearchByName(text)
                        .body()?.data!!
                    delay(500L)
                } else {
                    _animeList.value = emptyList()
                }

                _isSearching.update { false }

            } catch (e: java.lang.NullPointerException) {
                Log.e("HomeScreenViewModel", e.message.toString())
            } catch (e: java.net.UnknownHostException) {
                Log.e("HomeScreenViewModel", "Connection failed: " + e.message.toString())
            }


        }

    }

//    private val _imageMetadata = MutableStateFlow(0f)
//    val imageMetadata = _imageMetadata.asStateFlow()


}



