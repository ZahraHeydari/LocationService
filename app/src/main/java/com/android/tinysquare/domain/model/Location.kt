package com.android.tinysquare.domain.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
data class Location(
    val address: String?,
    val city: String?,
    val country: String?,
    val state: String?,
    val distance: Int?
) : Parcelable