package com.project.toko.detailScreen.viewModel

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.core.model.cache.DataCache
import com.project.toko.core.repository.MalApiService
import com.project.toko.homeScreen.model.newAnimeSearchModel.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailScreenViewModel @Inject constructor(
    private val malApiService: MalApiService, private val dataCache: DataCache
) : ViewModel() {

    //detailData
    private val _animeDetails = MutableStateFlow<Data?>(null)
    val animeDetails: StateFlow<Data?> get() = _animeDetails
    private val _loadedId = mutableStateOf(0)
    val loadedId = _loadedId

    //    private val animeCache = DataCacheSingleton.dataCache
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _scrollState: ScrollState by mutableStateOf(ScrollState(0))
    var scrollState = _scrollState

    private val _previousId = MutableStateFlow(0)
    var previousId = _previousId

    suspend fun onTapAnime(id: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            // Сохраните предыдущее id
            val previousId = _loadedId.value
            if (previousId != id) {
                _previousId.value = previousId
            }


            if (dataCache.containsId(id)) {
                _animeDetails.value = dataCache.getData(id)
                return@launch
            }
            try {
                _isSearching.value = true
                val response = malApiService.getDetailsFromAnime(id)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    _loadedId.value = id
                    if (data != null) {
                        withContext(Dispatchers.Main) {
                            _animeDetails.value = data
                            dataCache.setData(id, data)
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
        mutableMapOf<Int, List<com.project.toko.homeScreen.model.staffModel.Data>>()
    private val _staffList =
        MutableStateFlow<List<com.project.toko.homeScreen.model.staffModel.Data>>(emptyList())
    val staffList = _staffList.asStateFlow()

    suspend fun addStaffFromId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val cachedStaff = staffCache[id]
            if (cachedStaff != null) {
                _staffList.value = cachedStaff
                return@launch
            }

            try {
                val response = malApiService.getStaffFromId(id)
                if (response.isSuccessful) {
                    val staff = response.body()?.data ?: emptyList()
                    staffCache[id] = staff
                    _staffList.value = staff
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
        mutableMapOf<Int, List<com.project.toko.homeScreen.model.castModel.Data>>()
    private val _castList =
        MutableStateFlow<List<com.project.toko.homeScreen.model.castModel.Data>>(emptyList())
    val castList = _castList.asStateFlow()

    suspend fun addCastFromId(id: Int) {
        val cachedCharacters = castCache[id]
        if (cachedCharacters != null) {
            _castList.value = cachedCharacters
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
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
                Log.e("CastInDetailScreenVM", e.message.toString())
                // если произошла ошибка, присваиваем пустой список
                _castList.value = emptyList()
            }
        }
    }

}
