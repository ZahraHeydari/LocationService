package com.android.tinysquare.data.source.remote.base

import com.android.tinysquare.domain.model.DetailResponse
import com.android.tinysquare.domain.model.ExploreResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("venues/explore")
    suspend fun getVenues(
        @Query("ll") latLong: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): ExploreResponse


    @GET("venues/{id}")
    suspend fun getVenueDetail(
        @Path("id") venueId: String
    ): DetailResponse
}