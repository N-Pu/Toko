package com.example.animeapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterPicturesViewModel(malApiService: MalApiService.Companion) : ViewModel() {

    private val animeRepository = malApiService.api
    private val picturesCache = mutableMapOf<Int, List<com.example.animeapp.domain.characterPictures.Data>>()
    private val _picturesList = MutableStateFlow<List<com.example.animeapp.domain.characterPictures.Data>>(emptyList())
    val picturesList = _picturesList.asStateFlow()

    fun getPicturesFromId(id: Int) {
        viewModelScope.launch {
            val cachedPictures = picturesCache[id]
            if (cachedPictures != null) {
                _picturesList.value = cachedPictures
                return@launch
            }

            withContext(Dispatchers.IO) {
                try {
                    val response = animeRepository.getCharacterFullPictures(id)
                    if (response.isSuccessful) {
                        val pictures = response.body()?.data ?: emptyList()
                        picturesCache[id] = pictures
                        _picturesList.value = pictures
                    }
                } catch (e: Exception) {
                    Log.e("CharacterPicturesViewModel", e.message.toString())
                }
            }
        }
    }
}
