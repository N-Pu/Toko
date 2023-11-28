package com.project.toko.core.di.component

import androidx.compose.ui.Modifier
import com.project.toko.core.di.module.ModifierModule
import dagger.Component

@Component(modules = [ModifierModule::class])
interface ModifierComponent {
    fun providesModifier(): Modifier
}