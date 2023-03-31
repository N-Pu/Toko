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


    private val animeRepository = api

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    fun onSearchTextChange(text: String) {
        _searchText.value = text
        viewModelScope.launch {
            try {
                _animes.value = animeRepository.getAnimeSearchByName(text).body()?.data!!
                delay(700L)
            } catch (e: java.lang.NullPointerException) {
                Log.e("mainViewModel", e.message.toString())
            }


        }

    }

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _animes = MutableStateFlow<List<Data>>(emptyList())
    val animes = _animes.asStateFlow()


}



