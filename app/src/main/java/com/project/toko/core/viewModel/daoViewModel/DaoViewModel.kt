package com.project.toko.core.viewModel.daoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.core.dao.AnimeItem
import com.project.toko.core.dao.Dao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DaoViewModel @Inject constructor(private val dao: Dao) : ViewModel() {


    suspend fun addToCategory(animeItem: AnimeItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addToCategory(animeItem)
        }
    }

    fun getAnimeInCategory(category: String): Flow<List<AnimeItem>> {
        return dao.getAnimeInCategory(category)
    }

    suspend fun removeFromDataBase(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.removeFromDataBase(id)
        }
    }

    fun containsInDataBase(id: Int): Flow<Boolean> {
        return dao.containsInDataBase(id)
    }
}