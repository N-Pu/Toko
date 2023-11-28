package com.project.toko.core.di.component

import com.project.toko.core.dao.Dao
import com.project.toko.core.di.module.DatabaseModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [DatabaseModule::class])
interface DatabaseComponent {

    fun provideDao(): Dao

}