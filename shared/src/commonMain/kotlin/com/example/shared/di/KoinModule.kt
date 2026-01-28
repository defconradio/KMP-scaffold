package com.example.shared.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(appModule)
}

// Example module - define your shared dependencies here
val appModule = module {
    // single { MyRepository() }
    // factory { MyViewModel() }
}
