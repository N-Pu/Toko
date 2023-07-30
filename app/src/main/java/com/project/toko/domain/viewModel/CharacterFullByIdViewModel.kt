package com.project.toko.domain.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.domain.models.characterModel.Data
import com.project.toko.repository.MalApiService
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

   suspend fun getCharacterFromId(malId: Int) {
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
                    Log.e("CharacterFullByIdVM", e.message.toString())
                } finally {
                    _isSearching.value = false
                }
            }
        }


    //character album
    private val picturesCache = mutableMapOf<Int, List<com.project.toko.domain.models.characterPictures.Data>>()
    private val _picturesList = MutableStateFlow<List<com.project.toko.domain.models.characterPictures.Data>>(emptyList())
    val picturesList = _picturesList.asStateFlow()

    suspend  fun getPicturesFromId(id: Int) {
        val cachedPictures = picturesCache[id]
        if (cachedPictures != null) {
            _picturesList.value = cachedPictures
            return
        }

        viewModelScope.launch (Dispatchers.IO){

            try {
                val response = malApiService.getCharacterFullPictures(id)
                if (response.isSuccessful) {
                    val pictures = response.body()?.data ?: emptyList()
                    picturesCache[id] = pictures
                    _picturesList.value = pictures
                }
            } catch (e: Exception) {
                Log.e("CharacterPicturesVM", e.message.toString())
            }
        }
    }

}
