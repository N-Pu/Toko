package com.project.toko.daoScreen.daoViewModel

import androidx.compose.runtime.mutableStateOf
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

    private val _isSortedAlphabetically = mutableStateOf(false)
    val isSortedAlphabetically = _isSortedAlphabetically
    private val _isSortedByScore = mutableStateOf(false)
    val isSortedByScore = _isSortedByScore
    private val _isSortedByUsers = mutableStateOf(false)
    val isSortedByUsers = _isSortedByUsers
    private val _isAiredFrom = mutableStateOf(false)
    val isAiredFrom = _isAiredFrom

    private val _selectedType = mutableStateOf<String?>(null)
    val selectedType = _selectedType


    private val _isTvSelected = mutableStateOf(false)
    val isTvSelected = _isTvSelected
    private val _isMovieSelected = mutableStateOf(false)
    val isMovieSelected = _isMovieSelected
    private val _isOvaSelected = mutableStateOf(false)
    val isOvaSelected = _isOvaSelected
    private val _isSpecialSelected = mutableStateOf(false)
    val isSpecialSelected = _isSpecialSelected
    private val _isOnaSelected = mutableStateOf(false)
    val isOnaSelected = _isOnaSelected
    private val _isMusicSelected = mutableStateOf(false)
    val isMusicSelected = _isMusicSelected
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

//    fun getAnimeInCategory(category: String): Flow<List<AnimeItem>> {
//        return dao.getAnimeInCategory(category, searchText = _searchText.value ?: "")
//    }

    fun getAnimeInCategory(
        category: String,
        searchText: String,
        isSortedAlphabetically: Boolean,
        isSortedByScore: Boolean,
        isSortedByUsers: Boolean,
        isAiredFrom: Boolean,
        type: String?
    ): Flow<List<AnimeItem>> {
        return dao.getAnimeInCategory(
            category = category,
            searchText = searchText,
            isSortedAlphabetically = isSortedAlphabetically,
            isSortedByScore = isSortedByScore,
            isSortedByUsers = isSortedByUsers,
            isAiredFrom = isAiredFrom,
            type = type ?: ""
        )
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


//    fun sorting(
//        isSortedAlphabetically: MutableState<Boolean>,
//        isSortedByScore: MutableState<Boolean>,
//        isSortedByUsers: MutableState<Boolean>,
//        isAiredFrom: MutableState<Boolean>,
//        currentAnimeInSection: List<AnimeItem>
//    ): List<AnimeItem> {
//        var sortedList = currentAnimeInSection
//
//        if (isSortedAlphabetically.value) {
//            isSortedByScore.value = false
//            isSortedByUsers.value = false
//            isAiredFrom.value = false
//            sortedList = currentAnimeInSection.sortedBy { animeItem -> animeItem.animeName }
//        }
//
//        if (isSortedByScore.value) {
//            isSortedAlphabetically.value = false
//            isSortedByUsers.value = false
//            isAiredFrom.value = false
//            sortedList =
//                currentAnimeInSection.sortedByDescending { animeItem -> animeItem.score.toFloatOrNull() }
//        }
//        if (isSortedByUsers.value) {
//            isSortedAlphabetically.value = false
//            isSortedByScore.value = false
//            isAiredFrom.value = false
//            sortedList =
//                currentAnimeInSection.sortedByDescending { animeItem -> animeItem.scored_by.toFloatOrNull() }
//        }
//
//        if (isAiredFrom.value) {
//            isSortedAlphabetically.value = false
//            isSortedByScore.value = false
//            isSortedByUsers.value = false
//            sortedList =
//                currentAnimeInSection.sortedByDescending { animeItem -> animeItem.airedFrom }
//        }
//
//        return sortedList
//    }
}
