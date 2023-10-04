package com.project.toko.core.di.module

import android.content.Context
import androidx.navigation.NavHostController
import dagger.Module
import dagger.Provides

@Module
class NavHostControllerModule(private val context: Context) {

    @Provides
    fun provideNavHostController(): NavHostController = NavHostController(context)
//class NavHostControllerModule(private val context: Context) {
}