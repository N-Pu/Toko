package com.project.toko.characterDetailedScreen.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.characterDetailedScreen.model.characterFullModel.Data
import com.project.toko.core.repository.MalApiService
import com.project.toko.core.utils.connectionCheck.isInternetAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterFullByIdViewModel @Inject constructor(private val malApiService: MalApiService) :
    ViewModel() {
    private val charactersCache = mutableMapOf<Int, Data?>()
    private val _characterFull = MutableStateFlow<Data?>(null)
    val characterFull = _characterFull.asStateFlow()

    private val _loadedId = mutableIntStateOf(0)
    val loadedId = _loadedId

    private val _isLoading = mutableStateOf(false)
    var isLoading = _isLoading
    private suspend fun getCharacterFromId(malId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                _isLoading.value = true
                val response = malApiService.getCharacterFullFromId(malId)
                if (response.isSuccessful) {
                    val character = response.body()?.data
                    _loadedId.intValue = malId
                    charactersCache[malId] = character
                    _characterFull.value = character
                }
            } catch (e: Exception) {
                Log.e("CharacterFullByIdVM", e.message.toString())
            }
//            finally {
//                _isLoading.value = false
//            }
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

    private suspend fun getPicturesFromId(id: Int) {
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


    suspend fun loadAllInfo(id: Int, context: Context) {
        viewModelScope.launch {
            if (isInternetAvailable(context)) {
                _isLoading.value = true
                delay(300L)
                getCharacterFromId(id)
                delay(300L)
                getPicturesFromId(id)
                _isLoading.value = false
            } else {
                Toast.makeText(
                    context,
                    "No internet connection!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
