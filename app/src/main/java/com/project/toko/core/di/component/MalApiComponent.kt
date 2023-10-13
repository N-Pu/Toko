package com.project.toko.core.di.component

import com.project.toko.core.di.module.MalApiModule
import com.project.toko.core.repository.MalApiService
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [MalApiModule::class])
interface MalApiComponent {
    fun provideMalApiService(): MalApiService
}
