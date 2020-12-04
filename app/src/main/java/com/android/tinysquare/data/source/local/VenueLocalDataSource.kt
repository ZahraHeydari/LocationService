package com.android.tinysquare.data.source.local

import com.android.tinysquare.domain.model.Locale

interface VenueLocalDataSource {

    suspend fun getAllVenues(): List<Locale>?

    suspend fun insertAllVenues(list: List<Locale>?) : List<Long>?

}