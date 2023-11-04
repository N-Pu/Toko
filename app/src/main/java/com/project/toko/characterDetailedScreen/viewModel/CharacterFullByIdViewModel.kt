package com.project.toko.characterDetailedScreen.viewModel

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.characterDetailedScreen.model.characterFullModel.Data
import com.project.toko.core.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterFullByIdViewModel @Inject constructor(private val malApiService: MalApiService) :
    ViewModel() {
    private val charactersCache = mutableMapOf<Int, Data?>()
    private val _characterFull = MutableStateFlow<Data?>(null)
    val characterFull = _characterFull.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    private val _loadedId = mutableIntStateOf(0)
    val loadedId = _loadedId
    suspend fun getCharacterFromId(malId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isSearching.value = true
                val response = malApiService.getCharacterFullFromId(malId)
                if (response.isSuccessful) {
                    val character = response.body()?.data
                    _loadedId.intValue = malId
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
    private val picturesCache =
        mutableMapOf<Int, List<com.project.toko.characterDetailedScreen.model.characterPictures.CharacterPicturesData>>()
    private val _picturesList =
        MutableStateFlow<List<com.project.toko.characterDetailedScreen.model.characterPictures.CharacterPicturesData>>(
            emptyList()
        )
    val picturesList = _picturesList.asStateFlow()

    suspend fun getPicturesFromId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
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
