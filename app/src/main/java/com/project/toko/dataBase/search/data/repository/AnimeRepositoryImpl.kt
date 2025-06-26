package com.project.toko.dataBase.search.data.repository

import android.util.Log
import com.project.toko.core.domain.repository.MalApiService
import com.project.toko.dataBase.search.data.db.entity.AnimeEntity
import com.project.toko.dataBase.search.data.db.AppDatabase
import com.project.toko.dataBase.search.data.db.entity.AnimeOrderEntity
import com.project.toko.dataBase.search.data.db.entity.PaginationEntity
import com.project.toko.dataBase.search.data.db.entity.SearchQueryEntity
import com.project.toko.dataBase.search.data.remote.AnimeRepository
import com.project.toko.dataBase.search.data.remote.FilterParams
import com.project.toko.dataBase.search.data.remote.toTriple
import com.project.toko.dataBase.search.ui.viewmodel.InitialState
import com.project.toko.dataBase.search.ui.viewmodel.PaginationState
import com.project.toko.dataBase.search.ui.viewmodel.ScrollBehavior
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "AnimeRepositoryImpl"

class AnimeRepositoryImpl @Inject constructor(
    private val apiService: MalApiService, private val appDatabase: AppDatabase
) : AnimeRepository {

    private val searchQueriesDao by lazy { appDatabase.searchQueriesDao() }
    private val animeDao by lazy { appDatabase.animeDao() }
    private val paginationDao by lazy { appDatabase.paginationDao() }
    private val animeOrderDao by lazy { appDatabase.animeOrderDao() }
    private var previousQueryFilter = FilterParams()

    override suspend fun getAnimeList(
        queryFilter: FilterParams,
        scrollBehaviorStateFlow: MutableStateFlow<ScrollBehavior>
    ): Flow<InitialState> = flow {
        emit(InitialState.Loading)
        Log.d(TAG, "Starting getAnimeList for query: '$queryFilter'")

        val filterKey = queryFilter.toString()
        val isSameQuery = previousQueryFilter == queryFilter
        val scrollBehavior =
            if (isSameQuery) ScrollBehavior.KEEP_POSITION else ScrollBehavior.SCROLL_TO_TOP

        Log.d(TAG, filterKey)

        val majorId = searchQueriesDao.findQueryIdByFilters(filterKey)
        Log.d(
            TAG,
            "MAJOR_ID NULL OR NOT? ${if (majorId != null) "NOT NULL: '$majorId'" else "NULL"}"
        )

        scrollBehaviorStateFlow.emit(scrollBehavior)
        previousQueryFilter = queryFilter

        if (majorId != null) {
            emitAll(handleExistingQuery(majorId))
        } else {
            emitAll(handleNewQuery(queryFilter))
        }
    }.catch { e ->
        Log.e(TAG, "Exception in getAnimeList: ${e.message}", e)
        emit(InitialState.Error(e))
    }


    private fun handleExistingQuery(majorId: Long): Flow<InitialState> =
        animeDao.getListOfAnimeByMajorId(majorId)
            .map { list ->
                if (list.isEmpty()) InitialState.Empty
                else InitialState.Success(list)
            }
            .catch { e ->
                emit(InitialState.Error(e))
            }

    private suspend fun handleNewQuery(queryFilter: FilterParams): Flow<InitialState> = flow {
        val response = apiService.searchAnimeByName(
            query = queryFilter.query, page = 1,
            sfw = queryFilter.sfw,
            type = queryFilter.type,
            genres = queryFilter.genres,
            min_score = queryFilter.min_score,
            max_score = queryFilter.max_score,
            rating = queryFilter.rating,
            orderBy = queryFilter.orderBy,
            sort = queryFilter.sort
        )
        Log.d(TAG, "Initial API response received successfully")

        val newMajorId = createNewQueryEntry(queryFilter.toString())
        Log.d(TAG, "New majorId generated: $newMajorId")

        val pair = response.toTriple(majorId = newMajorId)
        Log.d(TAG, "Converted response to pair: ${pair.first.size} anime items")

        saveNewData(pair)

        emitAll(handleExistingQuery(newMajorId))
    }.catch { e ->
        emit(InitialState.Error(e))
    }

    override suspend fun deleteDataByFilterParam(filterParams: MutableStateFlow<FilterParams>) {
        try {
            val majorId = animeDao.findQueryIdByFilters(filterParams.value.toString())

            if (majorId != null) {
                animeDao.deleteAllDataByFilterJson(majorId)
                Log.e(TAG, "WORKED")
            } else {
                Log.e(TAG, "DIDN'T WORK")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error on deleting all data by filter json", e)
        }
    }

    override suspend fun canPaginate(queryFilter: FilterParams): Boolean {
        Log.d(TAG, "Checking pagination possibility for query: '$queryFilter'")
        val filterKey = queryFilter.toString()
        Log.d(TAG, filterKey)
        val majorId = searchQueriesDao.findQueryIdByFilters(filterKey)
        return if (majorId != null) {
            Log.d(TAG, "Query found, majorId: $majorId")
            val canPaginate = paginationDao.hasNextPage(majorId)
            Log.d(TAG, "Pagination result: $canPaginate")
            canPaginate
        } else {
            Log.d(TAG, "Query not found in database")
            false
        }
    }


    override suspend fun loadNextPage(
        filterParams: FilterParams,
        paginationState: MutableStateFlow<PaginationState>
    ) {
        paginationState.tryEmit(PaginationState.Empty)
        val filterKey = filterParams.toString()
        val majorId = searchQueriesDao.findQueryIdByFilters(filterKey)
        try {
            val pagination = majorId?.let { paginationDao.getPagination(it) }
            Log.d(TAG, "Retrieved pagination: ${pagination?.pagination}")

            if (pagination?.pagination?.hasNextPage == true) {
                paginationState.emit(PaginationState.Loading)
                val response = apiService.searchAnimeByName(
                    query = filterParams.query,
                    page = pagination.pagination.currentPage + 1,
                    sfw = filterParams.sfw,
                    type = filterParams.type,
                    genres = filterParams.genres,
                    min_score = filterParams.min_score,
                    max_score = filterParams.max_score,
                    rating = filterParams.rating,
                    orderBy = filterParams.orderBy,
                    sort = filterParams.sort
                )
                Log.d(TAG, "API response received successfully")
                val existingCount = animeOrderDao.getAnimeCountByMajorId(majorId)
                val triple = response.toTriple(majorId = majorId, existingPages = existingCount)
                Log.d(TAG, "Converted response to pair: ${triple.first.size} anime items")
                saveNewData(triple)
                paginationState.emit(PaginationState.Empty)
            } else {
                paginationState.emit(PaginationState.Empty)
                Log.d(TAG, "No more pages available, returning existing data")
            }
        } catch (e: java.net.SocketTimeoutException) {
            paginationState.emit(handleTimeoutErrorWhilePaginating(e))
        } catch (e: IOException) {
            paginationState.emit(handleNetworkErrorWhilePaginating(e))
        } catch (e: HttpException) {
            paginationState.emit(handleApiErrorWhilePaginating(e))
        }
    }


    private suspend fun saveNewData(triple: Triple<List<AnimeEntity>, PaginationEntity, List<AnimeOrderEntity>>) {
        // Сохраняем аниме
        animeDao.insert(triple.first)

        // Сохраняем порядок
        val orders = triple.third
        animeOrderDao.insertAll(orders)

        // Сохраняем пагинацию
        paginationDao.updateOrInsert(triple.second)
    }

    // Создание новой записи запроса в базе данных
    private suspend fun createNewQueryEntry(query: String): Long {
        val newQuery = SearchQueryEntity(filtersJson = query)
        Log.d(TAG, "Creating new query entry in database")
        return searchQueriesDao.insertQueryFilters(newQuery)
    }

    // Обработка ошибок сети
    private fun handleNetworkErrorWhilePaginating(e: IOException): PaginationState {
        Log.e(TAG, "Network error: ${e.message}", e)
        return PaginationState.Error(e)
    }

    private fun handleTimeoutErrorWhilePaginating(e: IOException): PaginationState {
        Log.e(TAG, "Timeout error: ${e.message}", e)
        return PaginationState.Error(e)
    }

    // Обработка ошибок API
    private fun handleApiErrorWhilePaginating(e: HttpException): PaginationState {
        return if (e.code() == 429) {
            Log.e(TAG, "API rate limit exceeded", e)
            PaginationState.Error(e)
        } else {
            Log.e(TAG, "API request failed with code ${e.code()}", e)
            PaginationState.Error(e)
        }
    }
}


