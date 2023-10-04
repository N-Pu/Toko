package com.project.toko.core.di.module

import android.content.Context
import androidx.room.Room
import com.project.toko.core.dao.Dao
import com.project.toko.core.dao.MainDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DatabaseModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideMainDb(): Dao = Room.databaseBuilder(
        context.applicationContext,
        MainDb::class.java,
        "Main.db"
    ).fallbackToDestructiveMigration().build().getDao()

}