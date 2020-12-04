package com.android.tinysquare.data.repository

import com.android.tinysquare.data.source.local.VenueLocalDataSource
import com.android.tinysquare.data.source.remote.VenueRemoteDataSource
import com.android.tinysquare.domain.model.DetailResponse
import com.android.tinysquare.domain.model.ExploreResponse
import com.android.tinysquare.domain.model.Locale
import com.android.tinysquare.domain.repository.VenueRepository


/**
 * This class is responsible to choose a source for fetching data
 * Local data will be fetched using [VenueLocalDataSource]
 * and Remote data using [VenueRemoteDataSource]
 *
 * @author ZARA
 * */
class VenueRepositoryImp constructor(
    private val venueRemoteDataSource: VenueRemoteDataSource,
    private val venueLocalDataSource: VenueLocalDataSource
) : VenueRepository {

    override suspend fun getVenues(ll: String, offset: Int, limit: Int): ExploreResponse {
        return venueRemoteDataSource.getVenues(ll, offset, limit)
    }

    override suspend fun getVenueDetail(id: String): DetailResponse? {
        return venueRemoteDataSource.getVenueDetail(id)
    }

    override suspend fun getAllVenuesFromDb(): List<Locale>? {
        return venueLocalDataSource.getAllVenues()
    }

    override suspend fun insertVenuesToDb(list: List<Locale>?): List<Long>?  {
        return venueLocalDataSource.insertAllVenues(list)
    }

}