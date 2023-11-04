package com.project.toko.personDetailedScreen.viewModel

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.personDetailedScreen.model.personFullModel.Data
import com.project.toko.core.repository.MalApiService
import com.project.toko.personDetailedScreen.model.personPictures.PersonPicturesData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PersonByIdViewModel @Inject constructor(malApiService: MalApiService) : ViewModel() {
    private val animeRepository = malApiService
    private val _personFull = MutableStateFlow<Data?>(null)
    private val personCache = mutableMapOf<Int, Data?>()
    val personFull = _personFull.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()


    private val _loadedId = mutableIntStateOf(0)
    val loadedId = _loadedId

    fun getPersonFromId(mal_id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isSearching.value = true
                val response = animeRepository.getPersonFullFromId(mal_id)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    personCache[mal_id] = data
                    _loadedId.intValue = mal_id
                    _personFull.value = data
                }
            } catch (e: Exception) {
                Log.e("StaffFullByIdViewModel", e.message.toString())
            } finally {
                _isSearching.value = false
            }
        }
    }
    //character album
    private val picturesCache =
        mutableMapOf<Int, List<PersonPicturesData>>()
    private val _picturesList =
        MutableStateFlow<List<PersonPicturesData>>(
            emptyList()
        )
    val picturesList = _picturesList.asStateFlow()

    suspend fun getPicturesFromId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = animeRepository.getPersonFullPictures(id)
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
