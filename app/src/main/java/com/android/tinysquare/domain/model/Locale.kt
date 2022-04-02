package com.android.tinysquare.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class Locale(
    @PrimaryKey val referralId: String,
    @Embedded val venue: Venue?
)