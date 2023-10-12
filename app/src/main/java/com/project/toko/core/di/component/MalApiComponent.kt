package com.project.toko.core.di.component

import android.content.Context
import com.project.toko.core.di.module.MalApiModule
import com.project.toko.core.repository.MalApiService
import dagger.Component
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import javax.inject.Singleton


@Singleton
@Component(modules = [MalApiModule::class])
interface MalApiComponent {
    fun context(): Context
    fun cache(): Cache
    fun cacheDirectory(): File
    fun provideLoggingInterceptor(): HttpLoggingInterceptor
    fun provideHttpClient(): OkHttpClient
    fun provideMalApiService(): MalApiService

}
