package com.project.toko.core.di.component

import androidx.navigation.NavHostController
import com.project.toko.core.di.module.NavHostControllerModule
import dagger.Component


@Component(modules = [NavHostControllerModule::class])
interface NavHostControllerComponent {

    fun provideNavHostController(): NavHostController
}