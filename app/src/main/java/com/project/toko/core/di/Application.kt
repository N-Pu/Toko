package com.project.toko.core.di

import android.app.Application
import com.project.toko.core.di.component.DaggerDataCacheComponent
import com.project.toko.core.di.component.DaggerDatabaseComponent
import com.project.toko.core.di.component.DaggerMalApiComponent
import com.project.toko.core.di.component.DaggerModifierComponent
import com.project.toko.core.di.component.DataCacheComponent
import com.project.toko.core.di.component.DatabaseComponent
import com.project.toko.core.di.component.MalApiComponent
import com.project.toko.core.di.component.ModifierComponent
import com.project.toko.core.di.module.DatabaseModule

class Application : Application() {

    val modifierComponent: ModifierComponent = DaggerModifierComponent.create()
    val daoComponent: DatabaseComponent = DaggerDatabaseComponent
        .builder()
        .databaseModule(DatabaseModule(this))
        .build()
    val malApiComponent: MalApiComponent = DaggerMalApiComponent.create()
    val dataCacheComponent: DataCacheComponent = DaggerDataCacheComponent.create()
}