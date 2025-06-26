package com.project.toko.core.data.settings.darkMode

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DarkModePreferencesModule {

    private const val TAG = "Dark Mode"

    @Provides
    @Singleton
    @Named("dark_mode")
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }
}

