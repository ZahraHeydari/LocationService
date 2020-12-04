package com.android.tinysquare.domain.repository


import com.android.tinysquare.domain.model.DetailResponse
import com.android.tinysquare.domain.model.ExploreResponse
import com.android.tinysquare.domain.model.Locale


interface VenueRepository {

    suspend fun getVenues(ll: String, offset: Int, limit: Int) : ExploreResponse

    suspend fun getVenueDetail(id: String): DetailResponse?

    suspend fun getAllVenuesFromDb(): List<Locale>?

    suspend fun insertVenuesToDb(list : List<Locale>?) : List<Long>?
}