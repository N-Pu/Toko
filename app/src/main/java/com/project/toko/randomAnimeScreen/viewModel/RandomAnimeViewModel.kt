package com.project.toko.randomAnimeScreen.viewModel

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.project.toko.core.repository.MalApiService
import com.project.toko.homeScreen.model.newAnimeSearchModel.AnimeSearchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RandomAnimeViewModel @Inject constructor(private val malApiService: MalApiService) :
    ViewModel() {
    private var isSearching = false
    private val _animeDetails = MutableStateFlow<AnimeSearchData?>(null)
    val animeDetails: StateFlow<AnimeSearchData?> get() = _animeDetails


    private val _cardIsShown = mutableStateOf(false)
    val cardIsShown = _cardIsShown

    private val _isNSFWActive = mutableStateOf(false)
    val isNSFWActive = _isNSFWActive
    suspend fun onTapRandomAnime() {
        if (isSearching) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isSearching = true
                val response = malApiService.getRandomAnime(!isNSFWActive.value)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        _animeDetails.value = data
                    }
                }
            } catch (e: Exception) {
                Log.e("RandomAnimeViewModel", e.toString())
            } finally {
                isSearching = false
            }
        }
    }
}
