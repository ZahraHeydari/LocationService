package com.android.tinysquare.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VenueDetail(
    var id: String?,
    var name: String?,
    var categories: List<Category>?,
    var location: DetailLocation?,
    var rating: Float?,
    var ratingColor: String?,
    var photos: Photos?
) {

    fun getPhoto(): String = try {
        val photoList = photos?.groups?.get(0)?.items
        if (photoList.isNullOrEmpty()) ""
        else photoList[0].let {
            it.prefix.plus(1024).plus(it.suffix)
        }
    } catch (e: IndexOutOfBoundsException) {
        e.printStackTrace()
        ""
    }

    fun getAddressLabel(): String = location?.getFullAddress() ?: ""

    fun getCategoryLabel() = categories?.get(0)?.name?.trim().toString()

    fun getVenueRating(): Float = if (rating == null) 0.0f else rating!!

    fun getVenueRatingColor(): String {
        return if (ratingColor.isNullOrEmpty()) "515151" else ratingColor!!
    }
}