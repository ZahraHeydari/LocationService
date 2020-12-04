package com.android.tinysquare.domain.usecase

import com.android.tinysquare.domain.base.SingleUseCase
import com.android.tinysquare.domain.model.UserLocation
import com.android.tinysquare.domain.repository.UserRepository

class InsertUserLocationToDbUseCase(private val userRepository: UserRepository) :
    SingleUseCase<Long?, UserLocation?>() {

    override suspend fun run(params: UserLocation?): Long? {
        return userRepository.insertLocation(params)
    }

}