package com.project.toko.core.domain.di.module

import android.content.Context
import com.project.toko.core.domain.repository.MalApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MalApiModule {


    @Provides
    @Singleton
    fun provideCacheDirectory(@ApplicationContext context: Context): File {
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
    fun provideHttpClient(
        cache: Cache,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
//            .addInterceptor(request)
            .addInterceptor(httpLoggingInterceptor).addInterceptor { chain ->
                val request = chain.request()
                // Retry up to 3 times on failure
                var response: Response? = null
                var retryCount = 0
                var lastException: Exception? = null

                while (retryCount < 3) {
                    try {
                        response = chain.proceed(request)
                        if (response.isSuccessful) {
                            return@addInterceptor response
                        }
                    } catch (e: Exception) {
                        lastException = e
                    }
                    retryCount++
                    if (retryCount < 3) {
//                        delay(1000L * retryCount) // Exponential backoff
                    }
                }

                throw lastException ?: IOException("Failed after 3 retries")
            }
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



