package com.android.tinysquare.domain.usecase

import com.android.tinysquare.domain.base.SingleUseCase
import com.android.tinysquare.domain.model.Locale
import com.android.tinysquare.domain.repository.VenueRepository

class GetVenuesFromDbUseCase(private val venueRepository: VenueRepository) :
    SingleUseCase<List<Locale>?, Any?>() {

    override suspend fun run(params: Any?): List<Locale>? {
        return venueRepository.getAllVenuesFromDb()
    }

}