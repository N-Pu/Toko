package com.project.toko.detailScreen.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.core.repository.MalApiService
import com.project.toko.core.utils.connectionCheck.isInternetAvailable
import com.project.toko.detailScreen.model.castModel.CastData
import com.project.toko.detailScreen.model.castModel.CastModel
import com.project.toko.detailScreen.model.detailModel.DetailData
import com.project.toko.detailScreen.model.detailModel.DetailScreenModel
import com.project.toko.detailScreen.model.pictureModel.DetailPicturesData
import com.project.toko.detailScreen.model.recommendationsModel.RecommendationsData
import com.project.toko.detailScreen.model.staffModel.StaffData
import com.project.toko.detailScreen.model.staffModel.StaffModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailScreenViewModel @Inject constructor(
    private val malApiService: MalApiService
) : ViewModel() {

    //detailData
    private val _animeDetails = MutableStateFlow<DetailData?>(null)
    val animeDetails: StateFlow<DetailData?> get() = _animeDetails
    private val _loadedId = mutableIntStateOf(0)
    val loadedId = _loadedId

    private val _scrollState: ScrollState by mutableStateOf(ScrollState(0))
    var scrollState = _scrollState

    private val _previousId = MutableStateFlow(0)
    private var previousId = _previousId

    private val _isLoading = mutableStateOf(false)
    var isLoading = _isLoading


    private val cachedDetailScreenData: MutableMap<Int, DetailScreenModel> = mutableMapOf()

    private suspend fun onTapAnime(id: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                // Сохраните предыдущее id
                val previousId = _loadedId.intValue
                if (previousId != id) {
                    _previousId.value = previousId
                }
                if (cachedDetailScreenData.contains(id)) {
                    _animeDetails.value = cachedDetailScreenData[id]!!.data
                    return@launch
                }

                val response = malApiService.getDetailsFromAnime(id)
                if (response.isSuccessful) {

                    val data = response.body()?.data
                    _loadedId.intValue = id
                    if (data != null) {
                        withContext(Dispatchers.Main) {
                            _animeDetails.value = data
                        }
                    }
                    cachedDetailScreenData[id] = response.body()!!
                }
            } catch (e: Exception) {
                Log.e("DetailScreenViewModel", e.message.toString())
            }
        }
    }


    //staff
    private val cachedStaffData: MutableMap<Int, StaffModel> = mutableMapOf()

    private val _staffList =
        MutableStateFlow<List<StaffData>>(emptyList())
    val staffList = _staffList.asStateFlow()

    private suspend fun addStaffFromId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                if (cachedStaffData.contains(id)) {
                    _staffList.value = cachedStaffData[id]!!.data
                    return@launch
                }

                val response = malApiService.getStaffFromId(id)
                if (response.isSuccessful) {
                    val staff = response.body()?.data ?: emptyList()
                    _staffList.value = staff

                    cachedStaffData[id] = response.body()!!
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
    private val cachedCastData: MutableMap<Int, CastModel> = mutableMapOf()

    private val _castList =
        MutableStateFlow<List<CastData>>(emptyList())
    val castList = _castList.asStateFlow()

    private suspend fun addCastFromId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (cachedCastData.contains(id)) {
                    _castList.value = cachedCastData[id]!!.data
                    return@launch
                }

                val response = malApiService.getCharactersFromId(id)
                if (response.isSuccessful) {
                    val characters = response.body()?.data ?: emptyList()
                    _castList.value = characters
                    cachedCastData[id] = response.body()!!

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
    private val cachedRecommendationsData: MutableMap<Int, List<RecommendationsData>> =
        mutableMapOf()

    private val _recommendationList = MutableStateFlow<List<RecommendationsData>>(emptyList())
    val recommendationList = _recommendationList.asStateFlow()

    private suspend fun addRecommendationsFromId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (cachedRecommendationsData.contains(id)) {
                    _recommendationList.value = cachedRecommendationsData[id]!!
                    return@launch
                }

                val response = malApiService.getRecommendationsFromAnime(id)
                if (response.isSuccessful) {
                    val recommendationData = response.body()?.data ?: emptyList()
                    _recommendationList.value = recommendationData
                    cachedRecommendationsData[id] = response.body()!!.data
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

    private val cachedPicturesData: MutableMap<Int, List<DetailPicturesData>> =
        mutableMapOf()
    private val _picturesData = MutableStateFlow<List<DetailPicturesData>>(emptyList())
    val picturesData = _picturesData.asStateFlow()
    private suspend fun showPictures(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (cachedPicturesData.contains(id)) {
                    _picturesData.value = cachedPicturesData[id]!!
                    return@launch
                }
                val response = malApiService.getDetailScreenPictures(id)
                if (response.isSuccessful) {
                    val pictures = response.body()?.data ?: emptyList()
                    _picturesData.value = pictures
                    cachedPicturesData[id] = response.body()!!.data
                } else if (response.code() == 404) {
                    // если получен ответ 404, присваиваем пустой список
                    _picturesData.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("Recommendations", e.message.toString())
                // если произошла ошибка, присваиваем пустой список
                _picturesData.value = emptyList()
            }
        }
    }


    suspend fun loadAllInfo(id: Int, context: Context) {
        viewModelScope.launch {


            val prevId = previousId.value
            if (id != prevId) {
                scrollState.scrollTo(0)
                previousId.value = id
            }
            if (isInternetAvailable(context)) {
//                _isLoading.value = true

                onTapAnime(id)
                delay(300)
                addStaffFromId(id)
                delay(300)
                addCastFromId(id)
                delay(1000L)
                addRecommendationsFromId(id)
                delay(1000L)
                showPictures(id)

//                _isLoading.value = false
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context, "No internet connection!", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    suspend fun refreshAndLoadAllInfo(id: Int, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {

            //cleaning current cache
            cachedDetailScreenData.remove(id)
            cachedCastData.remove(id)
            cachedStaffData.remove(id)
            cachedRecommendationsData.remove(id)
            cachedPicturesData.remove(id)

            val prevId = previousId.value
            if (id != prevId) {
                scrollState.scrollTo(0)
                previousId.value = id
            }
            if (isInternetAvailable(context)) {
                _isLoading.value = true

                onTapAnime(id)
                delay(300)
                addStaffFromId(id)
                delay(300)
                addCastFromId(id)
                delay(1000L)
                addRecommendationsFromId(id)
                delay(1000L)
                showPictures(id)

                _isLoading.value = false
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context, "No internet connection!", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
