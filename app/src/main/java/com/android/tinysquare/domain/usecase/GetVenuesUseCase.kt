package com.android.tinysquare.domain.usecase

import com.android.tinysquare.domain.base.SingleUseCase
import com.android.tinysquare.domain.model.ExploreResponse
import com.android.tinysquare.domain.repository.VenueRepository


class GetVenuesUseCase(private val venueRepository: VenueRepository)
    : SingleUseCase<ExploreResponse, String>() {

    var offset = 0
    private val limit = 20

    override suspend fun run(params: String?): ExploreResponse? {
        return params?.let { venueRepository.getVenues(it, offset, limit) }
    }

}