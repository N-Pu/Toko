package com.example.animeapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.characterModel.Data
import com.example.animeapp.domain.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CharacterFullByIdViewModel(malApiService: MalApiService.Companion) : ViewModel() {
    private val animeRepository = malApiService.api
    private val _characterFull = MutableStateFlow<Data?>(null)
    private val charactersCache = mutableMapOf<Int, Data?>()
    val characterFull = _characterFull.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    fun getCharacterFromId(mal_id: Int) {
        viewModelScope.launch {
            val cachedCharacters = charactersCache[mal_id]
            if (cachedCharacters != null) {
                _characterFull.value = cachedCharacters
                return@launch
            }

            withContext(Dispatchers.IO) {
                try {
                    _isSearching.value = true
                    val response = animeRepository.getCharacterFullFromId(mal_id)
                    if (response.isSuccessful) {
                        val character = response.body()?.data
                        charactersCache[mal_id] = character
                        _characterFull.value = character
                    }
                } catch (e: Exception) {
                    Log.e("CharacterFullByIdViewModel", e.message.toString())
                } finally {
                    _isSearching.value = false
                }
            }
        }
    }
}
