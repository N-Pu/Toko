package com.project.toko.core.di.component

import com.project.toko.core.di.module.MalApiModule
import com.project.toko.core.repository.MalApiService
import dagger.Component
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton


@Singleton
@Component(modules = [MalApiModule::class])
interface MalApiComponent {

    fun provideLoggingInterceptor(): HttpLoggingInterceptor

    fun provideHttpClient(): OkHttpClient

    fun provideMalApiService(): MalApiService

}
