package com.project.toko.core.di.module

import androidx.compose.ui.Modifier
import dagger.Module
import dagger.Provides

@Module
class ModifierModule {

    @Provides
    fun provideModifier(): Modifier = Modifier
}