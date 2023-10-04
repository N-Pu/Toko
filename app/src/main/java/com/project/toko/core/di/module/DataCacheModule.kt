package com.project.toko.core.di.module

import com.project.toko.core.model.cache.DataCache
import dagger.Module
import dagger.Provides


@Module
class DataCacheModule {

    @Provides
    fun provideDataCache(): DataCache = DataCache()
}