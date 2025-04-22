package com.project.toko.personDetailedScreen.ui.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.core.domain.repository.MalApiService
import com.project.toko.core.domain.util.connectionCheck.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PersonByIdViewModel @Inject constructor(val malApiService: MalApiService) : ViewModel() {
    private val _personFull =
        MutableStateFlow<com.project.toko.personDetailedScreen.data.model.personFullModel.Data?>(
            null
        )
    private val personCache =
        mutableMapOf<Int, com.project.toko.personDetailedScreen.data.model.personFullModel.Data?>()
    val personFull = _personFull.asStateFlow()
    private val _isLoading = mutableStateOf(false)
    var isLoading = _isLoading
    private val _loadedId = mutableIntStateOf(0)
//    val loadedId = _loadedId

    private suspend fun getPersonFromId(mal_id: Int) = withContext(Dispatchers.IO) {
        try {
            val response = malApiService.getPersonFullFromId(mal_id)
            if (response.isSuccessful) {
                val data = response.body()?.data
                personCache[mal_id] = data
                _loadedId.intValue = mal_id
                _personFull.value = data
            } else {
                throw IllegalArgumentException("Error response from server in person viewmodel")
            }
        } catch (e: Exception) {
            Log.e("StaffFullByIdViewModel", e.message.toString())
        }
    }

    //character album
    private val picturesCache =
        mutableMapOf<Int, List<com.project.toko.personDetailedScreen.data.model.personPictures.PersonPicturesData>>()
    private val _picturesList =
        MutableStateFlow<List<com.project.toko.personDetailedScreen.data.model.personPictures.PersonPicturesData>>(
            emptyList()
        )
    val picturesList = _picturesList.asStateFlow()

    private suspend fun getPicturesFromId(id: Int) = withContext(Dispatchers.IO) {
        try {
            val response = malApiService.getPersonFullPictures(id)
            if (response.isSuccessful) {
                val pictures = response.body()?.data ?: emptyList()
                picturesCache[id] = pictures
                _picturesList.value = pictures
            } else {
                throw IllegalArgumentException("Error image response from server in person viewmodel")
            }
        } catch (e: Exception) {
            Log.e("CharacterPicturesVM", e.message.toString())
        }
    }


    suspend fun loadAllInfo(id: Int, context: Context) = withContext(Dispatchers.IO) {
        if (isInternetAvailable(context)) {
            _isLoading.value = true
            delay(300L)
            getPersonFromId(id)
            delay(300L)
            getPicturesFromId(id)
            _isLoading.value = false
        } else {
            withContext(Dispatchers.Main.immediate)
            {
                Toast.makeText(
                    context,
                    "No internet connection!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
