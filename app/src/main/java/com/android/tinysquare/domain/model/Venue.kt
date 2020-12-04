package com.android.tinysquare.domain.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.TypeConverters
import com.android.tinysquare.data.source.local.base.CategoryTypeConverter
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
data class Venue(
    var id: String?,
    var name: String?,
    @TypeConverters(CategoryTypeConverter::class)
    var categories: List<Category>?,
    @Embedded var location: Location?
) : Parcelable {

    fun getIcon() = categories?.get(0)?.getIcon().toString()

    fun getCategoryLabel() = categories?.get(0)?.name?.trim().toString()

    fun getDistanceFormatted() =
        if (location?.distance ?: 0 <= 1000) location?.distance.toString().trim().plus(" M")
        else (location?.distance?.div(1000F)).toString().trim().plus(" Km")

}