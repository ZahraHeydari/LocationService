package com.android.tinysquare.domain.usecase

import com.android.tinysquare.domain.base.SingleUseCase
import com.android.tinysquare.domain.model.Locale
import com.android.tinysquare.domain.repository.VenueRepository


class InsertVenuesToDbUseCase(private val venueRepository: VenueRepository) :
    SingleUseCase<List<Long>?, List<Locale>?>() {

    override suspend fun run(params: List<Locale>?): List<Long>? {
        return venueRepository.insertVenuesToDb(params)
    }

}