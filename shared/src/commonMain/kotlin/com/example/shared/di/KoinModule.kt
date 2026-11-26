package com.example.shared.di

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}): KoinApplication = startKoin {
    appDeclaration()
    modules(platformModule, appModule, networkModule)
}

/**
 * Convenience overload for iOS (Swift) and Wasm entry points that do not need platform Koin DSL
 * or KoinApplication on their classpath.
 */
fun initKoin() {
    initKoin {}
}

/**
 * Initializes cross-platform Napier logging. Call once at app startup on each platform.
 */
fun initLogger() {
    Napier.base(DebugAntilog())
}

// Shared application dependencies (repositories, use cases, view models)
val appModule = module {
    // single { MyRepository(get()) }
    // factory { MyViewModel(get()) }
}
