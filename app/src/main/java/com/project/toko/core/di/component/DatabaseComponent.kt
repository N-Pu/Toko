package com.project.toko.core.di.component

import com.project.toko.core.dao.MainDb
import com.project.toko.core.di.module.DatabaseModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [DatabaseModule::class])
interface DatabaseComponent {

    fun provideDao(): MainDb

}