package com.project.toko.daoScreen.daoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.characterDetailedScreen.dao.CharacterItem
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.core.dao.Dao
import com.project.toko.personDetailedScreen.dao.PersonItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DaoViewModel @Inject constructor(private val dao: Dao) : ViewModel() {
    private val _searchText = MutableStateFlow<String?>(null)
    val searchText = _searchText.asStateFlow()

//    private val _currentCategory = mutableStateOf<String?>(null)
//        val currentCategory = _currentCategory

//    private val _currentAnimeList = MutableStateFlow<List<AnimeItem>>(emptyList())
//    val currentAnimeList = _currentAnimeList.asStateFlow()
    fun onSearchTextChange(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchText.value = text
        }
    }

    suspend fun addToCategory(animeItem: AnimeItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addToCategory(animeItem)
        }
    }

    fun getAnimeInCategory(category: String): Flow<List<AnimeItem>> {

        return dao.getAnimeInCategory(category, searchText = _searchText.value ?: "")
    }

    suspend fun removeFromDataBase(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.removeFromDataBase(id)
        }
    }

    fun containsInDataBase(id: Int): Flow<Boolean> {
        return dao.containsInDataBase(id)
    }

    fun containsItemIdInCategory(id: Int, categoryId: String): Flow<Boolean> {
        return dao.containsItemIdInCategory(id, categoryId)
    }

    fun sortAnimeByName(category: String): Flow<List<AnimeItem>> {
        return dao.sortAnimeByName(category)
    }

//    fun getAnimeListSortedByScore(): Flow<List<AnimeItem>>{
//        return dao.sortAnimeByScore()
//    }
//
//    fun getAnimeListSortedByScoredBy(): Flow<List<AnimeItem>> {
//        return dao.sortAnimeByScore()
//    }


    // Character
    fun isCharacterInDao(id: Int): Flow<Boolean> {
        return dao.isCharacterInDao(id)
    }

    suspend fun removeCharacterFromDataBase(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.removeCharacterFromDataBase(id)
        }
    }

    fun getAllCharacters(): Flow<List<CharacterItem>> {
        return dao.getAllCharacters(_searchText.value ?: "")
    }

    suspend fun addCharacter(characterItem: CharacterItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addCharacter(characterItem)
        }
    }

    // Person
    fun isPersonInDao(id: Int): Flow<Boolean> {
        return dao.isPersonInDao(id)
    }

    suspend fun removePersonFromDataBase(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.removePersonFromDataBase(id)
        }
    }

    fun getAllPeople(): Flow<List<PersonItem>> {
        return dao.getAllPeople(_searchText.value ?: "")
    }

    suspend fun addPerson(personItem: PersonItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addPerson(personItem)
        }
    }
}