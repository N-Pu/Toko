package com.project.toko.randomAnimeScreen.presentation_layer.viewModel

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.project.toko.core.data.settings.nsfw.SafeForWorkManager
import com.project.toko.core.domain.repository.MalApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RandomAnimeViewModel @Inject constructor(
    private val malApiService: MalApiService,
    private val safeForWorkManager: SafeForWorkManager) :
    ViewModel() {
    private var isSearching = false
    private val _animeDetails = MutableStateFlow<com.project.toko.homeScreen.data.model.newAnimeSearchModel.AnimeSearchData?>(null)
    val animeDetails: StateFlow<com.project.toko.homeScreen.data.model.newAnimeSearchModel.AnimeSearchData?> get() = _animeDetails


    private val _cardIsShown = mutableStateOf(false)
    val cardIsShown = _cardIsShown

    private val _isNSFWActive = mutableStateOf(false)
    val isNSFWActive = _isNSFWActive
//    init {
//        viewModelScope.launch {
//            onTapRandomAnime()
//        }
//    }



    suspend fun onTapRandomAnime() = withContext(Dispatchers.IO) {
        if (isSearching) return@withContext
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

    val sfwState = safeForWorkManager.isNSFWActive


    fun toggleSFW(){
        safeForWorkManager.toggleSFW()
    }
}
