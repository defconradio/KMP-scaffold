package com.example.androidApp

import android.app.Application
import com.example.shared.di.initKoin
import com.example.shared.di.initLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initLogger()

        initKoin {
            androidLogger()
            androidContext(this@AndroidApp)
        }
    }
}
