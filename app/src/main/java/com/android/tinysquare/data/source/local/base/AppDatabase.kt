package com.android.tinysquare.data.source.local.base

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.tinysquare.data.source.local.base.AppDatabase.Companion.DB_VERSION
import com.android.tinysquare.data.source.local.base.dao.LocaleDao
import com.android.tinysquare.data.source.local.base.dao.UserLocationDao
import com.android.tinysquare.domain.model.Locale
import com.android.tinysquare.domain.model.UserLocation

/**
 * To manage data items that can be accessed and updated
 * & also maintain relationships between them
 *
 * @Created by ZARA
 */
@Database(entities = [Locale::class, UserLocation::class], version = DB_VERSION, exportSchema = false)
@TypeConverters(CategoryTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val localeDao: LocaleDao
    abstract val userLocationDao : UserLocationDao

    companion object {
        const val DB_NAME = "TinySquareApp.db"
        const val DB_VERSION = 1
    }
}