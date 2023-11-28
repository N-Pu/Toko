package com.project.toko.daoScreen.daoViewModel

import android.content.Context
import android.widget.Toast
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

class DaoViewModel @Inject constructor(private val dao: Dao, private val context: Context) :
    ViewModel() {
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
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(
                context,
                "${animeItem.animeName} is in ${animeItem.category} category!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

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


    suspend fun removeFromDataBase(animeItem: AnimeItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.removeFromDataBase(animeItem.id!!)
        }
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(
                context,
                "${animeItem.animeName} was removed from ${animeItem.category} category!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

//    fun containsInDataBase(id: Int): Flow<Boolean> {
//        return dao.containsInDataBase(id)
//    }

    fun containsItemIdInCategory(id: Int, categoryId: String): Flow<Boolean> {
        return dao.containsItemIdInCategory(id, categoryId)
    }

    // Character
    fun isCharacterInDao(id: Int): Flow<Boolean> {
        return dao.isCharacterInDao(id)
    }

    suspend fun removeCharacterFromDataBase(characterItem: CharacterItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.removeCharacterFromDataBase(characterItem.id!!)
        }
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(
                context,
                "${characterItem.name} was removed to database!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getAllCharacters(): Flow<List<CharacterItem>> {
        return dao.getAllCharacters(_searchText.value ?: "")
    }

    suspend fun addCharacter(characterItem: CharacterItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addCharacter(characterItem)
        }
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(
                context,
                "${characterItem.name} was added to database!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Person
    fun isPersonInDao(id: Int): Flow<Boolean> {
        return dao.isPersonInDao(id)
    }

    suspend fun removePersonFromDataBase(personItem: PersonItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.removePersonFromDataBase(personItem.id!!)
        }
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(
                context,
                "${personItem.name} was removed from database!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getAllPeople(): Flow<List<PersonItem>> {
        return dao.getAllPeople(_searchText.value ?: "")
    }

    suspend fun addPerson(personItem: PersonItem) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addPerson(personItem)
        }
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(
                context,
                "${personItem.name} was added from database!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    fun getCategoryForId(id: Int): Flow<String?> {
        return dao.getCategoryForId(id)
    }
}
