package com.project.toko.core.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.toko.characterDetailedScreen.dao.CharacterItem
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.personDetailedScreen.dao.PersonItem
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

    // Функция для добавления аниме в категорию
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCategory(animeItem: AnimeItem)

    @Query(
        "SELECT * FROM animeItems " +
                "WHERE category = :category AND " +
                "(CASE WHEN :searchText = '' THEN 1 ELSE animeName LIKE '%' || :searchText || '%' END) " +
                "AND (CASE WHEN :type = '' THEN 1 ELSE type = :type END) " +
                "ORDER BY " +
                "CASE WHEN :isSortedAlphabetically = 1 THEN animeName END ASC, " +
                "CASE WHEN :isSortedByScore = 1 THEN score END DESC, " +
                "CASE WHEN :isSortedByUsers = 1 THEN scored_by END ASC, " +
                "CASE WHEN :isAiredFrom = 1 THEN airedFrom END DESC"
    )
    fun getAnimeInCategory(
        category: String,
        searchText: String,
        isSortedAlphabetically: Boolean,
        isSortedByScore: Boolean,
        isSortedByUsers: Boolean,
        isAiredFrom: Boolean,
        type: String
    ): Flow<List<AnimeItem>>


    @Query("DELETE FROM animeItems WHERE id = :id")
    suspend fun removeFromDataBase(id: Int)

    // Функция для проверки наличия ID в базе данных
    @Query("SELECT EXISTS(SELECT 1 FROM animeItems WHERE id = :id LIMIT 1)")
    fun containsInDataBase(id: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM animeItems WHERE id = :id AND category = :categoryId LIMIT 1)")
    fun containsItemIdInCategory(id: Int, categoryId: String): Flow<Boolean>

    @Query("SELECT category FROM animeItems WHERE id = :id")
    fun getCategoryForId(id: Int): Flow<String?>

    // character table
    @Query("SELECT EXISTS(SELECT 1 FROM characterItem WHERE id = :id LIMIT 1)")
    fun isCharacterInDao(id: Int): Flow<Boolean>

    @Query("DELETE FROM characterItem WHERE id = :id")
    suspend fun removeCharacterFromDataBase(id: Int)

    @Query("SELECT * FROM characterItem WHERE (CASE WHEN :searchText = '' THEN  1 ELSE name LIKE '%' || :searchText || '%' END) ORDER BY name")
    fun getAllCharacters(searchText: String): Flow<List<CharacterItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharacter(characterItem: CharacterItem)

    // character table
    @Query("SELECT EXISTS(SELECT 1 FROM personItem WHERE id = :id LIMIT 1)")
    fun isPersonInDao(id: Int): Flow<Boolean>

    @Query("DELETE FROM personItem WHERE id = :id")
    suspend fun removePersonFromDataBase(id: Int)

    @Query("SELECT * FROM personItem WHERE (CASE WHEN :searchText = '' THEN 1 ELSE name LIKE '%' || :searchText || '%' END) ORDER BY name")
    fun getAllPeople(searchText: String): Flow<List<PersonItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPerson(personItem: PersonItem)
}
