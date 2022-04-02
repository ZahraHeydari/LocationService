package com.android.tinysquare.presentation.main

import com.android.tinysquare.domain.model.Venue

/**
 * To make an interaction between [MainActivity] & its child
 * */
interface OnMainActivityCallback {
    fun navigateToDetail(venue: Venue)
}