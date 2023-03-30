package com.example.animeapp.viewModel


import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.repository.ApiService
import com.example.animeapp.domain.repository.ApiService.Companion.api
import com.example.animeapp.domain.searchModel.AnimeSearchModel
import com.example.animeapp.domain.searchModel.Data
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class MainViewModel : ViewModel() {

    //    [THIS CODE IS WORKING]
    private val animeRepository = ApiService.api
//    private val _dataListResponse = MutableLiveData<List<Data>>()
//    val dataListResponse = _dataListResponse
//    private var errorMessage: String by mutableStateOf("")
//
//
//    fun inputNameOfSearch(nameOfAnime: String) {
//        try {
//            viewModelScope.launch {
//                dataListResponse.value = api.getAnimeSearchByName(nameOfAnime).body()?.data
//            }
//        } catch (e: Exception) {
//            errorMessage = e.message.toString()

//            Log.e("ViewModel", errorMessage)
//        }
//    }

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    fun onSearchTextChange(text: String) {
        _searchText.value = text
        viewModelScope.launch {
            try {
                _animes.value = animeRepository.getAnimeSearchByName(text).body()?.data!!
                delay(1000L)
            } catch (e: java.lang.NullPointerException) {
                Log.e("mainViewModel", e.message.toString())
            }


        }

    }

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()


    private val _animes = MutableStateFlow<List<Data>>(emptyList())
    val animes = _animes.asStateFlow()
//    fun inputAnime(animeName: String) {
//        viewModelScope.launch {
//            _animes.value = animeRepository.getAnimeSearchByName(animeName).body()?.data!!
//        }
//
//    }

//    private val _animes = viewModelScope.launch {
//        MutableStateFlow(ApiService.api.getAnimeSearchByName(searchText).body().data)
//    }
//    val animes = _animes
//    val anime = searchText.combine(_anime) { cur_text, cur_anime_list ->
//        if (cur_text.isBlank()) {
//            cur_anime_list
//        }
//    }.stateIn(
//        viewModelScope, SharingStarted.WhileSubscribed(5000),
//        _anime.value
//    )

}



