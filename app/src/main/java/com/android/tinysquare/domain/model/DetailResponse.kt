package com.android.tinysquare.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetailResponse(
    var meta: Meta?,
    var response: VenueDetailResponse?
)