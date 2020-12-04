package com.android.tinysquare.data.source.local

import com.android.tinysquare.data.source.local.base.AppDatabase
import com.android.tinysquare.domain.model.UserLocation

class UserLocalDataSourceImp(private val appDatabase: AppDatabase) : UserLocalDataSource {

    override suspend fun insertLocation(location: UserLocation?): Long? {
        return appDatabase.userLocationDao.insert(location)
    }

    override suspend fun loadLocation(): UserLocation? {
        return appDatabase.userLocationDao.load()
    }
}