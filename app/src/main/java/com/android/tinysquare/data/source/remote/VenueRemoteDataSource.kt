package com.android.tinysquare.data.source.remote

import com.android.tinysquare.domain.model.DetailResponse
import com.android.tinysquare.domain.model.ExploreResponse

interface VenueRemoteDataSource {

    suspend fun getVenues(ll: String, offset: Int, limit: Int): ExploreResponse

    suspend fun getVenueDetail(id: String): DetailResponse?
}