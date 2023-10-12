package com.project.toko.core.di.module

import android.content.Context
import com.project.toko.core.di.Application
import com.project.toko.core.repository.MalApiService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Module
class MalApiModule @Inject constructor(private val application: Application) {


    @Provides
    @Singleton
    fun provideContext(): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideCacheDirectory(context: Context): File {
        return File(context.cacheDir, "http_cache")
    }

    @Provides
    @Singleton
    fun provideCache(cacheDirectory: File): Cache {
        return Cache(cacheDirectory, 250L * 1024L * 1024L) // 250 MiB
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(provideLoggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideMalApiService(okHttpClient: OkHttpClient): MalApiService {
        return Retrofit.Builder()
            .baseUrl(MalApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MalApiService::class.java)
    }
}



