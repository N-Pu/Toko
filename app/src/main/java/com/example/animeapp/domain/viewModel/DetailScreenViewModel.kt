package com.example.animeapp.domain.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.models.newAnimeSearchModel.Data
import com.example.animeapp.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailScreenViewModel(private val malApiService: MalApiService) : ViewModel() {

    //detailData
    private val _animeDetails = MutableStateFlow<Data?>(null)
    val animeDetails: StateFlow<Data?> get() = _animeDetails
    private val _loadedIds = mutableSetOf<Int>()
    val loadedIds = {
        if (_loadedIds.isNotEmpty())
            _loadedIds.last()
        else
            0
    }
    val animeCache = mutableMapOf<Int, Data>()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    suspend fun onTapAnime(id: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            val cachedAnime = animeCache[id]
            if (cachedAnime != null) {
                _animeDetails.value = cachedAnime
                return@launch
            }
            if (_loadedIds.contains(id)) {
                return@launch
            }

            try {
                _isSearching.value = true
                val response = malApiService.getDetailsFromAnime(id)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        withContext(Dispatchers.Main) {
                            _animeDetails.value = data
                            _loadedIds.add(id)
                            animeCache[id] = data
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("DetailScreenViewModel", e.message.toString())
            } finally {
                _isSearching.value = false
            }
        }
    }


    //staff
    private val staffCache =
        mutableMapOf<Int, List<com.example.animeapp.domain.models.staffModel.Data>>()
    private val _staffList =
        MutableStateFlow<List<com.example.animeapp.domain.models.staffModel.Data>>(emptyList())
    val staffList = _staffList.asStateFlow()

    suspend fun addStaffFromId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val cachedStaff = staffCache[id]
            if (cachedStaff != null) {
                _staffList.value = cachedStaff
                return@launch
            }

            try {
                viewModelScope.launch(Dispatchers.IO) {
//                withContext(Dispatchers.IO) {
                    val response = malApiService.getStaffFromId(id)
                    if (response.isSuccessful) {
                        val staff = response.body()?.data ?: emptyList()
                        staffCache[id] = staff
                        _staffList.value = staff
                    }
                }
            } catch (e: Exception) {
                Log.e("StaffViewModel", e.message.toString())
            } finally {
                if (_staffList.value.isEmpty()) {
                    _staffList.value = emptyList()
                }
            }
        }
    }


    // cast
    private val castCache =
        mutableMapOf<Int, List<com.example.animeapp.domain.models.castModel.Data>>()
    private val _castList =
        MutableStateFlow<List<com.example.animeapp.domain.models.castModel.Data>>(emptyList())
    val castList = _castList.asStateFlow()

    suspend fun addCastFromId(id: Int) {
        val cachedCharacters = castCache[id]
        if (cachedCharacters != null) {
            _castList.value = cachedCharacters
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = malApiService.getCharactersFromId(id)
                    if (response.isSuccessful) {
                        val characters = response.body()?.data ?: emptyList()
                        castCache[id] = characters
                        _castList.value = characters
                    } else if (response.code() == 404) {
                        // если получен ответ 404, присваиваем пустой список
                        _castList.value = emptyList()
                    }
                } catch (e: Exception) {
                    Log.e("CastInDetailScreenViewModel", e.message.toString())
                    // если произошла ошибка, присваиваем пустой список
                    _castList.value = emptyList()
                }
            }
        }
    }
}
