package com.project.toko.detailScreen.viewModel

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.core.repository.MalApiService
import com.project.toko.detailScreen.model.castModel.CastData
import com.project.toko.detailScreen.model.detailModel.DetailData
import com.project.toko.detailScreen.model.recommendationsModel.RecommendationsData
import com.project.toko.detailScreen.model.staffModel.StaffData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailScreenViewModel @Inject constructor(
    private val malApiService: MalApiService,
) : ViewModel() {

    //detailData
    private val _animeDetails = MutableStateFlow<DetailData?>(null)
    val animeDetails: StateFlow<DetailData?> get() = _animeDetails
    private val _loadedId = mutableStateOf(0)
    val loadedId = _loadedId

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

            try {
                _isSearching.value = true
                val response = malApiService.getDetailsFromAnime(id)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    _loadedId.value = id
                    if (data != null) {
                        withContext(Dispatchers.Main) {
                            _animeDetails.value = data
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

    private val _staffList =
        MutableStateFlow<List<StaffData>>(emptyList())
    val staffList = _staffList.asStateFlow()

    suspend fun addStaffFromId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = malApiService.getStaffFromId(id)
                if (response.isSuccessful) {
                    val staff = response.body()?.data ?: emptyList()
                    _staffList.value = staff
                }
            } catch (e: Exception) {
                Log.e("Staff", e.message.toString())
            } finally {
                if (_staffList.value.isEmpty()) {
                    _staffList.value = emptyList()
                }
            }
        }
    }


    // cast
    private val _castList =
        MutableStateFlow<List<CastData>>(emptyList())
    val castList = _castList.asStateFlow()

    suspend fun addCastFromId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = malApiService.getCharactersFromId(id)
                if (response.isSuccessful) {
                    val characters = response.body()?.data ?: emptyList()
                    _castList.value = characters
                } else if (response.code() == 404) {
                    // если получен ответ 404, присваиваем пустой список
                    _castList.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("Cast", e.message.toString())
                // если произошла ошибка, присваиваем пустой список
                _castList.value = emptyList()
            }
        }
    }


    // recommendations
    private val _recommendationList = MutableStateFlow<List<RecommendationsData>>(emptyList())
    val recommendationList = _recommendationList.asStateFlow()

    suspend fun addRecommendationsFromId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = malApiService.getRecommendationsFromAnime(id)
                if (response.isSuccessful) {
                    val recommendationData = response.body()?.data ?: emptyList()
                    _recommendationList.value = recommendationData
                } else if (response.code() == 404) {
                    // если получен ответ 404, присваиваем пустой список
                    _recommendationList.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("Recommendations", e.message.toString())
                // если произошла ошибка, присваиваем пустой список
                _recommendationList.value = emptyList()
            }
        }
    }
}
