package com.example.animeapp.domain.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.models.characterModel.Data
import com.example.animeapp.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterFullByIdViewModel(private val malApiService: MalApiService) : ViewModel() {
    private val charactersCache = mutableMapOf<Int, Data?>()
    private val _characterFull = MutableStateFlow<Data?>(null)
    val characterFull = _characterFull.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    fun getCharacterFromId(malId: Int) {
        val cachedCharacter = charactersCache[malId]
        if (cachedCharacter != null) {
            _characterFull.value = cachedCharacter
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

                try {
                    _isSearching.value = true
                    val response = malApiService.getCharacterFullFromId(malId)
                    if (response.isSuccessful) {
                        val character = response.body()?.data
                        charactersCache[malId] = character
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
