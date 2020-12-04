package com.android.tinysquare.domain.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Photo(
    val id: String?,
    val prefix: String?,
    val suffix: String?,
    val height: Int?,
    val width: Int?
)