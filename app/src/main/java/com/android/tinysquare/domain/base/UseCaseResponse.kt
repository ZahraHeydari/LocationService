package com.android.tinysquare.domain.base

import com.android.tinysquare.domain.model.ApiError

interface UseCaseResponse<Type> {

    fun onSuccess(result: Type?)

    fun onError(error: ApiError)
}