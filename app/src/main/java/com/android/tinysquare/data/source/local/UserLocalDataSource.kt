package com.android.tinysquare.data.source.local

import com.android.tinysquare.domain.model.UserLocation

interface UserLocalDataSource {

    suspend fun insertLocation(location: UserLocation?): Long?

    suspend fun loadLocation(): UserLocation?
}