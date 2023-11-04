package com.project.toko.core.viewModel.daoViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.characterDetailedScreen.dao.CharacterItem
import com.project.toko.favoritesScreen.dao.AnimeItem
import com.project.toko.core.dao.Dao
import com.project.toko.personDetailedScreen.dao.PersonItem
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

    fun containsItemIdInCategory(id: Int, categoryId: String): Flow<Boolean> {
        return dao.containsItemIdInCategory(id, categoryId)
    }

    fun isCharacterInDao(id: Int): Flow<Boolean>{
        return dao.isCharacterInDao(id)
    }
    suspend fun removeCharacterFromDataBase(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            dao.removeCharacterFromDataBase(id)
        }
    }
    fun getCharacter(): Flow<List<CharacterItem>>{
        return dao.getCharacter()
    }

    suspend fun addCharacter(characterItem: CharacterItem){
        viewModelScope.launch(Dispatchers.IO) {
            dao.addCharacter(characterItem)
        }
    }


    fun isPersonInDao(id: Int): Flow<Boolean>{
        return dao.isPersonInDao(id)
    }
    suspend fun removePersonFromDataBase(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            dao.removePersonFromDataBase(id)
        }
    }
    fun getPerson(): Flow<List<PersonItem>>{
        return dao.getPerson()
    }

    suspend fun addPerson(personItem: PersonItem){
        viewModelScope.launch(Dispatchers.IO) {
            dao.addPerson(personItem)
        }
    }
}