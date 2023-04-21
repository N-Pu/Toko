package com.example.animeapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.repository.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharactersViewModel : ViewModel() {
    private val animeRepository = ApiService.api
    private val _charactersList =
        MutableStateFlow<List<com.example.animeapp.domain.charactersModel.Data>>(emptyList())
    val charactersList = _charactersList.asStateFlow()


    fun addCharacterAndSeyu(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = animeRepository.getCharactersFromId(id)
                    if (response.isSuccessful) {
                        _charactersList.value = response.body()?.data ?: emptyList()
                    }
//                    } else {
//                        _charactersList.value = emptyList()
//                    }
                } catch (e: Exception) {
                    Log.e("DetailScreenViewModel", e.message.toString())
                }
            }
        }
    }
}