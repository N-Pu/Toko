package com.project.toko.core.di.component

import com.project.toko.core.di.module.DataCacheModule
import com.project.toko.core.model.cache.DataCache
import dagger.Component


@Component(modules = [DataCacheModule::class])
interface DataCacheComponent {

    fun provideDataCache(): DataCache
}