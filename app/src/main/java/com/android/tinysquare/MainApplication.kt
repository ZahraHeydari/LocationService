package com.android.tinysquare

import android.app.Application
import androidx.multidex.MultiDex
import com.android.tinysquare.di.appModule
import com.android.tinysquare.di.databaseModule
import com.android.tinysquare.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this) //To avoid build error - for app with over 64k methods

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(appModule, networkModule, databaseModule))
        }
    }
}