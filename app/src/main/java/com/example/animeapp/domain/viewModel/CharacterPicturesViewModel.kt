
package com.example.animeapp.domain.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.models.characterPictures.Data
import com.example.animeapp.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterPicturesViewModel(private val malApiService: MalApiService) : ViewModel() {
    private val picturesCache = mutableMapOf<Int, List<Data>>()
    private val _picturesList = MutableStateFlow<List<Data>>(emptyList())
    val picturesList = _picturesList.asStateFlow()

    fun getPicturesFromId(id: Int) {
        val cachedPictures = picturesCache[id]
        if (cachedPictures != null) {
            _picturesList.value = cachedPictures
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = malApiService.getCharacterFullPictures(id)
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
