package com.project.toko.homeScreen.ui.viewModel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.project.toko.core.domain.util.connectionCheck.isInternetAvailable
import com.project.toko.core.data.dao.MainDb
import com.project.toko.core.domain.repository.MalApiService
import com.project.toko.savedScreen.data.dao.AnimeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val malApiRepository: MalApiService,
    private val dao: MainDb,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val emptyItem =
        com.project.toko.homeScreen.data.model.newAnimeSearchModel.Items(0, 0, 0)
    private val emptyNewAnimeSearchModel =
        com.project.toko.homeScreen.data.model.newAnimeSearchModel.NewAnimeSearchModel(
            data = ArrayList(),
            pagination = com.project.toko.homeScreen.data.model.newAnimeSearchModel.Pagination(
                has_next_page = false, emptyItem, last_visible_page = 0, current_page = 0
            )
        )


    private val _isLoadingSearch = mutableStateOf(false)
    var isLoadingSearch = _isLoadingSearch


    private val _isNSFWActive = mutableStateOf(false)




    private val _isTabMenuOpen = mutableStateOf(false)
    val isTabMenuOpen = _isTabMenuOpen


    private val _topTrendingAnime = MutableStateFlow(emptyNewAnimeSearchModel)
    val topTrendingAnime = _topTrendingAnime.asStateFlow()

    private val _topAiringAnime = MutableStateFlow(emptyNewAnimeSearchModel)
    val topAiringAnime = _topAiringAnime.asStateFlow()

    private val _topUpcomingAnime = MutableStateFlow(emptyNewAnimeSearchModel)
    val topUpcomingAnime = _topUpcomingAnime.asStateFlow()


    private val cachedTopTrendingAnime: MutableMap<String, com.project.toko.homeScreen.data.model.newAnimeSearchModel.NewAnimeSearchModel> =
        mutableMapOf()

    private val _loadingSectionTopAiring = mutableStateOf(false)
    val loadingSectionTopAiring = _loadingSectionTopAiring

    private val _loadingSectionTopUpcoming = mutableStateOf(false)
    val loadingSectionTopUpcoming = _loadingSectionTopUpcoming

    private val _loadingSectionTopTrending = mutableStateOf(false)
    val loadingSectionTopTrending = _loadingSectionTopTrending
    private suspend fun getTopAnime(
        filter: String,
        limit: Int,
        data: MutableStateFlow<com.project.toko.homeScreen.data.model.newAnimeSearchModel.NewAnimeSearchModel>,
        loadingCurrentSection: MutableState<Boolean>,
        sfw: Boolean
    ) = withContext(Dispatchers.IO) {
        try {
            val cachedData = cachedTopTrendingAnime[filter]
            if (cachedData != null) {
                // Если данные уже есть в кэше, используем их
                data.value = cachedTopTrendingAnime[filter]!!
            } else {
                loadingCurrentSection.value = true
                // Если данные отсутствуют в кэше, делаем запрос к API
                val response = malApiRepository.getTenTopAnime(filter, limit, sfw).body()
                val newData = response ?: emptyNewAnimeSearchModel
                loadingCurrentSection.value = false

                // Сохраняем новые данные в кэше
                cachedTopTrendingAnime[filter] = newData

                data.value = newData
            }
        } catch (e: Exception) {
            // Если произошла ошибка, показываем сообщение
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context, e.message, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    var isDialogShown by mutableStateOf(false)
        private set


    fun showListOfWatching(): Flow<List<AnimeItem>> {
        return dao.getDao().getLastTenAnimeFromWatchingSection()
    }

    fun showLastAdded(): Flow<List<AnimeItem>> {
        return dao.getDao().getLastTenAddedAnime()
    }

    suspend fun loadAllSections(context: Context) = withContext(Dispatchers.IO) {
        if (isInternetAvailable(context)) {
            getTopAnime(
                "bypopularity",
                25,
                _topTrendingAnime,
                loadingSectionTopTrending,
                !_isNSFWActive.value
            )
            delay(500L)
            getTopAnime(
                "airing",
                25,
                _topAiringAnime,
                loadingSectionTopAiring,
                !_isNSFWActive.value
            )
            delay(500L)
            getTopAnime(
                "upcoming",
                25,
                _topUpcomingAnime,
                loadingSectionTopUpcoming,
                !_isNSFWActive.value
            )
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context, "No internet connection!", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}



