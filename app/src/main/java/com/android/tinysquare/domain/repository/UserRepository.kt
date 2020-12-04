package com.android.tinysquare.domain.repository

import com.android.tinysquare.domain.model.UserLocation

interface UserRepository {

    suspend fun insertLocation(location: UserLocation?): Long?

    suspend fun loadUserLocation(): UserLocation?
}