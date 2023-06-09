package com.example.animeapp.domain.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.models.detailModel.Data
import com.example.animeapp.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailScreenViewModel(private val malApiService: MalApiService) : ViewModel() {
    private val _animeDetails = MutableStateFlow<Data?>(null)
    val animeDetails: StateFlow<Data?> get() = _animeDetails
    private val loadedIds = mutableSetOf<Int>()
    val animeCache = mutableMapOf<Int, Data>()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    fun onTapAnime(id: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            val cachedAnime = animeCache[id]
            if (cachedAnime != null) {
                _animeDetails.value = cachedAnime
                return@launch
            }
            if (loadedIds.contains(id)) {
                return@launch
            }

            try {
                _isSearching.value = true
                val response = malApiService.getDetailsFromAnime(id)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        withContext(Dispatchers.Main) {
                            _animeDetails.value = data
                            loadedIds.add(id)
                            animeCache[id] = data
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("DetailScreenViewModel", e.message.toString())
            } finally {
                _isSearching.value = false
            }
        }
    }
}
