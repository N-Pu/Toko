package com.project.toko.core.data.settings.nsfw

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
object SafeForWorkPreferencesModule {

    private const val TAG = "SFW mode"

    @Provides
    @Singleton
    @Named("sfw")
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }
}
