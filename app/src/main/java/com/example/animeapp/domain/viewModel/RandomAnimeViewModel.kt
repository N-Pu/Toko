package com.example.animeapp.domain.viewModel

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.models.cache.DataCacheSingleton
import com.example.animeapp.domain.models.newAnimeSearchModel.Data
import com.example.animeapp.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RandomAnimeViewModel(private val malApiService: MalApiService) :
    ViewModel() {
    //    private val animeCache = DetailDataCacheImpl(context)
    private val animeCache = DataCacheSingleton.dataCache
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
                        if (animeCache.containsId(data.mal_id)) {
                            _animeDetails.value = animeCache.getData(data.mal_id)
                            return@launch
                        }
                        animeCache.setData(data.mal_id, data)
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
