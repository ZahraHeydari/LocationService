package com.android.tinysquare.presentation.detail


import androidx.lifecycle.viewModelScope
import com.android.tinysquare.domain.base.UseCaseResponse
import com.android.tinysquare.domain.model.ApiError
import com.android.tinysquare.domain.model.DetailResponse
import com.android.tinysquare.domain.model.VenueDetail
import com.android.tinysquare.domain.usecase.GetVenueDetailUseCase
import com.android.tinysquare.presentation.base.BaseViewModel
import com.android.tinysquare.presentation.util.SingleLiveEvent


class DetailViewModel(private val getVenueDetailUseCase: GetVenueDetailUseCase) : BaseViewModel() {

    private var _venueDetailData = SingleLiveEvent<VenueDetail>()
    val venueDetail = _venueDetailData


    fun getVenueDetail(id: String) {
        showLoading(true)

        getVenueDetailUseCase.invoke(viewModelScope, id, object : UseCaseResponse<DetailResponse> {
            override fun onSuccess(result: DetailResponse?) {

                showLoading(false)
                _venueDetailData.value = result?.response?.venue
            }

            override fun onError(error: ApiError) {
                showLoading(false)
                handleError(error)
            }
        })
    }
}