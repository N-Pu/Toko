package com.project.toko.dataBase.search.data.di

import android.content.Context
import androidx.room.Room
import com.project.toko.dataBase.search.data.db.dao.AnimeDao
import com.project.toko.dataBase.search.data.db.AppDatabase
import com.project.toko.dataBase.search.data.db.dao.PaginationDao
import com.project.toko.dataBase.search.data.db.dao.SearchQueriesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// DatabaseModule.kt
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "anime_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideAnimeDao(database: AppDatabase): AnimeDao {
        return database.animeDao()
    }

    @Provides
    @Singleton
    fun providePaginationDao(database: AppDatabase): PaginationDao {
        return database.paginationDao()
    }

    @Provides
    @Singleton
    fun provideSearchQueries(database: AppDatabase): SearchQueriesDao {
        return database.searchQueriesDao()
    }

}