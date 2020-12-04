package com.android.tinysquare.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Group(
    val items: List<Locale>?,
    val name: String?,
    val type: String?
)