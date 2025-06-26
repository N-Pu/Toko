package com.project.toko.savedScreen.ui.daoViewModel

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.project.toko.characterDetailedScreen.data.model.dao.CharacterItem
import com.project.toko.core.data.dao.MainDb
import com.project.toko.savedScreen.data.dao.AnimeItem
import com.project.toko.savedScreen.data.dao.FavoriteItem
import com.project.toko.personDetailedScreen.data.dao.PersonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DaoViewModel @Inject constructor(
    private val mainDb: MainDb,
    @ApplicationContext private val context: Context
) :
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
            _searchText.value = text
        } catch (e: Exception) {
            Toast.makeText(
                context,
                e.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    suspend fun addToCategory(animeItem: AnimeItem) = withContext(Dispatchers.IO) {
        try {
            mainDb.getDao().addToCategory(animeItem)
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    "${animeItem.animeName} is in ${animeItem.category} category!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main.immediate) {
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


    suspend fun removeFromDataBase(animeItem: AnimeItem) = withContext(Dispatchers.IO) {
        try {
            mainDb.getDao().removeFromDataBase(animeItem.id!!)
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    "${animeItem.animeName} was removed from ${animeItem.category} category!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main.immediate) {
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

    suspend fun removeCharacterFromDataBase(characterItem: CharacterItem) =
        withContext(Dispatchers.IO) {
            try {
                mainDb.getDao().removeCharacterFromDataBase(characterItem.id!!)
                withContext(Dispatchers.Main.immediate) {
                    Toast.makeText(
                        context,
                        "${characterItem.name} was removed to database!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main.immediate) {
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

    suspend fun addCharacter(characterItem: CharacterItem) = withContext(Dispatchers.IO) {
        try {
            mainDb.getDao().addCharacter(characterItem)
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    "${characterItem.name} was added to database!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main.immediate) {
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

    suspend fun removePersonFromDataBase(personItem: PersonItem) = withContext(Dispatchers.IO) {
        try {
            mainDb.getDao().removePersonFromDataBase(personItem.id!!)
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    "${personItem.name} was removed from database!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main.immediate) {
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

    suspend fun addPerson(personItem: PersonItem) = withContext(Dispatchers.IO) {
        try {
            mainDb.getDao().addPerson(personItem)
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    "${personItem.name} was added from database!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    suspend fun removeFromFavorite(favoriteItem: FavoriteItem) = withContext(Dispatchers.IO) {
        try {
            mainDb.getDao().removeFromFavorite(favoriteItem.id!!)
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    "${favoriteItem.animeName} was removed from FAVORITE category!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    suspend fun addToFavorite(favoriteItem: FavoriteItem) = withContext(Dispatchers.IO) {
        try {
            mainDb.getDao().addToFavorite(favoriteItem)
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    "${favoriteItem.animeName} is in Favorite category!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main.immediate) {
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


    suspend fun deleteAnimeByCategory(category: String) = withContext(Dispatchers.IO) {
        try {
            mainDb.getDao().deleteAnimeByCategory(category)
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    "All anime were deleted from $category!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    suspend fun deleteAllCharacters() = withContext(Dispatchers.IO) {
        try {
            mainDb.getDao().deleteAllCharacters()
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    "All characters were deleted!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    suspend fun deleteAllPeople() = withContext(Dispatchers.IO) {
        try {
            mainDb.getDao().deleteAllPeople()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "All people were deleted!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    suspend fun deleteAllFavorite() = withContext(Dispatchers.IO) {
        try {
            mainDb.getDao().deleteAllFavorite()
            withContext(Dispatchers.Main.immediate) {
                Toast.makeText(
                    context,
                    "All anime were deleted from Favorite!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main.immediate) {
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
            Toast.makeText(
                context,
                e.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

}
