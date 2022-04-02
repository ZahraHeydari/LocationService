package com.android.tinysquare.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoGroups(
    val count: Int?,
    val items: List<Photo>?,
    val name: String?,
    val type: String?
)