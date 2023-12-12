package com.project.toko.daoScreen.daoViewModel

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.characterDetailedScreen.dao.CharacterItem
import com.project.toko.core.dao.MainDb
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.daoScreen.dao.FavoriteItem
import com.project.toko.personDetailedScreen.dao.PersonItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class DaoViewModel @Inject constructor(private val mainDb: MainDb, private val context: Context) :
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


    private val _currentSelectedAnimeListType = mutableStateOf("")
    val currentSelectedAnimeListType = _currentSelectedAnimeListType


    private val _lastSwipedAnime = mutableStateOf(
        AnimeItem(
            id = null,
            animeName = "",
            score = "",
            scored_by = "",
            animeImage = "",
            status = "",
            rating = "",
            secondName = "",
            airedFrom = "",
            category = null,
            type = "",
            createdAt = 0
        )
    )
    var lastSwipedAnime = _lastSwipedAnime

    private val _lastSwipedInFavorite = mutableStateOf(
        FavoriteItem(
            id = null,
            animeName = "",
            score = "",
            scored_by = "",
            animeImage = "",
            status = "",
            rating = "",
            secondName = "",
            airedFrom = "",
            type = "",
            createdAt = 0
        )
    )
    var lastSwipedInFavorite = _lastSwipedInFavorite
    fun onSearchTextChange(text: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                _searchText.value = text
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    suspend fun addToCategory(animeItem: AnimeItem) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                mainDb.getDao().addToCategory(animeItem)
            }
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "${animeItem.animeName} is in ${animeItem.category} category!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
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
        return mainDb.getDao().getAnimeInCategory(
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
        try {
            viewModelScope.launch(Dispatchers.IO) {
                mainDb.getDao().removeFromDataBase(animeItem.id!!)
            }
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "${animeItem.animeName} was removed from ${animeItem.category} category!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun containsItemIdInCategory(id: Int, categoryId: String): Flow<Boolean> {
        return mainDb.getDao().containsItemIdInCategory(id, categoryId)
    }

    // Character
    fun isCharacterInDao(id: Int): Flow<Boolean> {
        return mainDb.getDao().isCharacterInDao(id)
    }

    suspend fun removeCharacterFromDataBase(characterItem: CharacterItem) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                mainDb.getDao().removeCharacterFromDataBase(characterItem.id!!)
            }
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "${characterItem.name} was removed to database!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun getAllCharacters(): Flow<List<CharacterItem>> {
        return mainDb.getDao().getAllCharacters(_searchText.value ?: "")
    }

    suspend fun addCharacter(characterItem: CharacterItem) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                mainDb.getDao().addCharacter(characterItem)
            }
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "${characterItem.name} was added to database!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // Person
    fun isPersonInDao(id: Int): Flow<Boolean> {
        return mainDb.getDao().isPersonInDao(id)
    }

    suspend fun removePersonFromDataBase(personItem: PersonItem) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                mainDb.getDao().removePersonFromDataBase(personItem.id!!)
            }
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "${personItem.name} was removed from database!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun getAllPeople(): Flow<List<PersonItem>> {
        return mainDb.getDao().getAllPeople(_searchText.value ?: "")
    }

    suspend fun addPerson(personItem: PersonItem) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                mainDb.getDao().addPerson(personItem)
            }
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "${personItem.name} was added from database!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    suspend fun removeFromFavorite(favoriteItem: FavoriteItem) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                mainDb.getDao().removeFromFavorite(favoriteItem.id!!)
            }
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "${favoriteItem.animeName} was removed from FAVORITE category!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    suspend fun addToFavorite(favoriteItem: FavoriteItem) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                mainDb.getDao().addToFavorite(favoriteItem)
            }
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "${favoriteItem.animeName} is in Favorite category!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun containsInFavorite(id: Int): Flow<Boolean> {
        return mainDb.getDao().containsInFavorite(id)
    }

    fun getAnimeInFavorite(
        searchText: String,
        isSortedAlphabetically: Boolean,
        isSortedByScore: Boolean,
        isSortedByUsers: Boolean,
        isAiredFrom: Boolean,
        type: String
    ): Flow<List<FavoriteItem>> {
        return mainDb.getDao().getAnimeInFavorite(
            searchText = searchText,
            isSortedAlphabetically = isSortedAlphabetically,
            isSortedByScore = isSortedByScore,
            isSortedByUsers = isSortedByUsers,
            isAiredFrom = isAiredFrom,
            type = type ?: ""
        )
    }


    suspend fun deleteAnimeByCategory(category: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                mainDb.getDao().deleteAnimeByCategory(category)
            }
            viewModelScope.launch{
                Toast.makeText(
                    context,
                    "All anime were deleted from $category!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    suspend fun deleteAllCharacters() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                mainDb.getDao().deleteAllCharacters()
            }
            viewModelScope.launch{
                Toast.makeText(
                    context,
                    "All characters were deleted!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    suspend fun deleteAllPeople() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                mainDb.getDao().deleteAllPeople()
            }
            viewModelScope.launch{
                Toast.makeText(
                    context,
                    "All people were deleted!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    suspend fun deleteAllFavorite() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                mainDb.getDao().deleteAllFavorite()

            }
            viewModelScope.launch{
                Toast.makeText(
                    context,
                    "All anime were deleted from Favorite!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }



    fun exportDB(DATABASE_NAME: String, packageName: String) {
        val data = Environment.getDataDirectory()
        val currentDBPath = "/data/$packageName/databases/$DATABASE_NAME"

        val sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val backupRootPath = File(sd, "Toko_Saved_Data")

        if (!backupRootPath.exists()) {
            backupRootPath.mkdirs()
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
        val dateFolderName = dateFormat.format(Date())
        val backupFolderPath = File(backupRootPath, dateFolderName)

        if (!backupFolderPath.exists()) {
            backupFolderPath.mkdirs()
        }

        val dbFiles = listOf(currentDBPath, "$currentDBPath-shm", "$currentDBPath-wal")
        val backupFileNames = listOf("Main.db", "Main.db-shm", "Main.db-wal")

        try {
            for ((index, dbFile) in dbFiles.withIndex()) {
                val currentDB = File(data, dbFile)
                val backupDB = File(backupFolderPath, backupFileNames[index])

                if (currentDB.exists()) {
                    val source = FileInputStream(currentDB).channel
                    val destination = FileOutputStream(backupDB).channel
                    destination.transferFrom(source, 0, source.size())
                    source.close()
                    destination.close()
                }
            }
            Toast.makeText(
                context,
                "Data was saved at $backupFolderPath!",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: IOException) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}
