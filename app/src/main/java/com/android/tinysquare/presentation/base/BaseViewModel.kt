package com.android.tinysquare.presentation.base


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tinysquare.domain.model.ApiError
import com.android.tinysquare.presentation.util.SingleLiveEvent
import kotlinx.coroutines.cancel


open class BaseViewModel : ViewModel() {

    private var _isLoadingData = SingleLiveEvent<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoadingData
    private var _networkError = SingleLiveEvent<ApiError>()
    val networkError = _networkError


    fun showLoading(isVisible: Boolean) {
        _isLoadingData.value = isVisible
    }

    fun handleError(error: ApiError) {
        _networkError.value = error
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }


}