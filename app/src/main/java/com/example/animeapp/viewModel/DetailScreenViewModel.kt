package com.example.animeapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.detailModel.Data
import com.example.animeapp.domain.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailScreenViewModel(malApiService: MalApiService.Companion) : ViewModel() {
    private val animeRepository = malApiService.api
    private val _animeDetails = MutableStateFlow<Data?>(null)
    val animeDetails: StateFlow<Data?> get() = _animeDetails
    private val loadedIds = mutableMapOf<Int, Boolean>()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    fun onTapAnime(id: Int) {
        if (loadedIds[id] == true) {
            return // Если данные уже загружены, выходим
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _isSearching.value = true
                    loadedIds.clear()
                    val response = animeRepository.getDetailsFromAnime(id)
                    if (response.isSuccessful) {
                        response.body()?.data.let { data ->
                            withContext(Dispatchers.Main) {
                                _animeDetails.value = data
                                loadedIds[id] = true // Помечаем, что данные загружены
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
}
