package com.android.tinysquare.domain.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Icon(
    val prefix: String?,
    val suffix: String?
) : Parcelable