package com.example.animeapp.domain.viewModel

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.models.detailModel.Data
import com.example.animeapp.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RandomAnimeViewModel(private val malApiService: MalApiService) : ViewModel() {
    private val animeCache = DetailScreenViewModel(malApiService).animeCache

    private var isSearching = false

    private val _animeDetails = MutableStateFlow<Data?>(null)
    val animeDetails: StateFlow<Data?> get() = _animeDetails

    fun onTapRandomAnime() {
        if (isSearching) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                isSearching = true
                val response = malApiService.getRandomAnime()
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        if (animeCache.containsKey(data.mal_id)) {
                            _animeDetails.value = animeCache[data.mal_id]
                            return@launch
                        }
                        animeCache[data.mal_id] = data
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