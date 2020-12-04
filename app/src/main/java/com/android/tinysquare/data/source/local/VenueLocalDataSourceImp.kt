package com.android.tinysquare.data.source.local

import com.android.tinysquare.data.source.local.base.AppDatabase
import com.android.tinysquare.domain.model.Locale

class VenueLocalDataSourceImp(private val appDatabase: AppDatabase) : VenueLocalDataSource {

    override suspend fun getAllVenues(): List<Locale>? {
        return appDatabase.localeDao.loadAll()
    }

    override suspend fun insertAllVenues(list : List<Locale>?) : List<Long>? {
        return appDatabase.localeDao.deleteAllThenInsertNewLocales(list)
    }
}