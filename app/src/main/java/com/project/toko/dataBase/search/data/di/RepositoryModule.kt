package com.project.toko.dataBase.search.data.di

import com.project.toko.dataBase.search.data.remote.AnimeRepository
import com.project.toko.dataBase.search.data.repository.AnimeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAnimeRepository(impl: AnimeRepositoryImpl): AnimeRepository
}
