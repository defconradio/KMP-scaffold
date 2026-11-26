package com.example.shared.di

import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    // Define Android-specific dependencies here (e.g., DataStore, platform services)
}

