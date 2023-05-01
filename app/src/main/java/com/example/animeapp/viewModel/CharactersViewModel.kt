package com.example.animeapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.charactersModel.Data
import com.example.animeapp.domain.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharactersViewModel(malApiService: MalApiService.Companion) : ViewModel() {
//    private val animeRepository = MalApiService.api
    val animeRepository = malApiService.api

    private val characterCache = mutableMapOf<Int, List<Data>>()

    private val _charactersList = MutableStateFlow<List<Data>>(emptyList())
    val charactersList = _charactersList.asStateFlow()

    fun addCharacterAndSeyu(id: Int) {
        viewModelScope.launch {

            val cachedCharacters = characterCache[id]
            if (cachedCharacters != null) {
                _charactersList.value = cachedCharacters
                return@launch
            }

            withContext(Dispatchers.IO) {
                try {
                    characterCache.clear()
                    val response = animeRepository.getCharactersFromId(id)
                    if (response.isSuccessful) {
                        val characters = response.body()?.data ?: emptyList()
                        characterCache[id] = characters
                        _charactersList.value = characters
                    }
                } catch (e: Exception) {
                    Log.e("CharactersViewModel", e.message.toString())
                }
            }
        }
    }
}
