package com.project.toko.dataBase.search.data.di.popUpDialogModule

import com.project.toko.dataBase.search.data.remote.PopUpDialogRepository
import com.project.toko.dataBase.search.data.repository.PopUpDialogRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PopUpDialogModule {
    @Binds
    abstract fun bindPopUpDialogRepositoryImpl(impl: PopUpDialogRepositoryImpl): PopUpDialogRepository
}