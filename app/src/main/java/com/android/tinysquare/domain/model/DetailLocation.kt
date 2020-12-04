package com.android.tinysquare.domain.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class DetailLocation(
    val address: String?,
    val city: String?,
    val country: String?,
    val state: String?,
    val crossStreet: String?
) : Parcelable {

    fun getFullAddress(): String =
        "Address: ${country ?: ""} ${state ?: ""} ${city ?: ""} ${crossStreet ?: ""}"
}