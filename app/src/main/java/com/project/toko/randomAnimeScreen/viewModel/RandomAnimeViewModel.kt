package com.project.toko.randomAnimeScreen.viewModel

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.project.toko.core.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RandomAnimeViewModel @Inject constructor(private val malApiService: MalApiService) :
    ViewModel() {
    private var isSearching = false
    private val _animeDetails = MutableStateFlow<com.project.toko.homeScreen.model.newAnimeSearchModel.Data?>(null)
    val animeDetails: StateFlow<com.project.toko.homeScreen.model.newAnimeSearchModel.Data?> get() = _animeDetails

   suspend fun onTapRandomAnime() {
        if (isSearching) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                isSearching = true
                val response = malApiService.getRandomAnime()
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
