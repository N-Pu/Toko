package com.project.toko.core.di

import android.app.Application
import com.project.toko.core.di.component.DaggerDatabaseComponent
import com.project.toko.core.di.component.DaggerMalApiComponent
import com.project.toko.core.di.component.DaggerModifierComponent
import com.project.toko.core.di.component.DatabaseComponent
import com.project.toko.core.di.component.MalApiComponent
import com.project.toko.core.di.component.ModifierComponent
import com.project.toko.core.di.module.DatabaseModule
import com.project.toko.core.di.module.MalApiModule
import javax.inject.Inject

class Application @Inject constructor() : Application() {

    val modifierComponent: ModifierComponent = DaggerModifierComponent.create()
    val daoComponent: DatabaseComponent =
        DaggerDatabaseComponent.builder().databaseModule(DatabaseModule(this)).build()
    val malApiComponent: MalApiComponent =
        DaggerMalApiComponent.builder().malApiModule(MalApiModule(this)).build()
//    val svgComponent: SvgComponent = DaggerSvgComponent.builder().build()

    val context = this
}