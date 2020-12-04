package com.android.tinysquare.data.source.remote

import com.android.tinysquare.data.source.remote.base.ApiService
import com.android.tinysquare.domain.model.DetailResponse
import com.android.tinysquare.domain.model.ExploreResponse

class VenueRemoteDataSourceImp(private val apiService: ApiService) : VenueRemoteDataSource {


    override suspend fun getVenues(ll: String, offset: Int, limit: Int): ExploreResponse {
        return apiService.getVenues(ll, offset, limit)
    }

    override suspend fun getVenueDetail(id: String): DetailResponse? {
        return apiService.getVenueDetail(id)
    }
}