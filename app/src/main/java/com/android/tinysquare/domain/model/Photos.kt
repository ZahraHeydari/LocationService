package com.android.tinysquare.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photos(
    val count: Int,
    val groups: List<PhotoGroups>?
)