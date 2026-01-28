package com.example.androidApp

import android.app.Application
import com.example.shared.di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Napier Logger
        Napier.base(DebugAntilog())

        initKoin {
            androidLogger()
            androidContext(this@AndroidApp)
        }
    }
}
