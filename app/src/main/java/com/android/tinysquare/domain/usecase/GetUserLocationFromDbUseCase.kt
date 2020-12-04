package com.android.tinysquare.domain.usecase

import com.android.tinysquare.domain.base.SingleUseCase
import com.android.tinysquare.domain.model.UserLocation
import com.android.tinysquare.domain.repository.UserRepository

class GetUserLocationFromDbUseCase(private val userRepository: UserRepository) :
    SingleUseCase<UserLocation?,Any?>() {

    override suspend fun run(params: Any?): UserLocation? {
        return userRepository.loadUserLocation()
    }


}