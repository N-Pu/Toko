package com.example.animeapp.viewModel


import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.repository.ApiService.Companion.api
import com.example.animeapp.domain.searchModel.Data
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class MainViewModel : ViewModel() {


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
                    _animeList.value = animeRepository.getAnimeSearchByName(text).body()?.data!!
                    delay(700L)
                } else {
                    _animeList.value = emptyList()
                }
            } catch (e: java.lang.NullPointerException) {
                Log.e("mainViewModel", e.message.toString())
            } catch (e: java.net.UnknownHostException) {
                Log.e("mainViewModel", "Connection failed: " + e.message.toString())
            }


        }

    }


}



