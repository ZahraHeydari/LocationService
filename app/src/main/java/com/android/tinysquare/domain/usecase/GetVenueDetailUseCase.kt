package com.android.tinysquare.domain.usecase

import com.android.tinysquare.domain.base.SingleUseCase
import com.android.tinysquare.domain.model.DetailResponse
import com.android.tinysquare.domain.repository.VenueRepository

class GetVenueDetailUseCase(private val venueRepository: VenueRepository)
    : SingleUseCase<DetailResponse, String>() {

    override suspend fun run(params: String?): DetailResponse? {
        return params?.let { venueRepository.getVenueDetail(it) }
    }

}