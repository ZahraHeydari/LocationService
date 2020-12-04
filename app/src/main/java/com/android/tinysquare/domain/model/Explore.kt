package com.android.tinysquare.domain.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Explore(
    val groups: List<Group>?,
    val headerFullLocation: String?,
    val headerLocation: String?,
    val totalResults: Int?
)