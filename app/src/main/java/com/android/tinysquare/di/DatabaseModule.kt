package com.android.tinysquare.di

import android.app.Application
import androidx.room.Room
import com.android.tinysquare.data.source.local.base.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { createAppDatabase(get()) }
}

internal fun createAppDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        AppDatabase.DB_NAME
    ).build()
}