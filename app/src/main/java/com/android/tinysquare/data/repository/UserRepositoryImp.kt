package com.android.tinysquare.data.repository

import com.android.tinysquare.data.source.local.UserLocalDataSource
import com.android.tinysquare.data.source.remote.UserRemoteDataSource
import com.android.tinysquare.domain.model.UserLocation
import com.android.tinysquare.domain.repository.UserRepository

/**
 * To choose a source for fetching data relating user such as Location
 * [UserLocalDataSource] for local data
 * [UserRemoteDataSource] for remote data
 *
 * @author ZARA
 * */
class UserRepositoryImp constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {

    override suspend fun insertLocation(location: UserLocation?): Long? {
        return userLocalDataSource.insertLocation(location)
    }

    override suspend fun loadUserLocation(): UserLocation? {
        return userLocalDataSource.loadLocation()
    }
}