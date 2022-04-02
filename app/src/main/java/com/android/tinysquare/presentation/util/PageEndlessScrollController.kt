package com.android.tinysquare.presentation.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PageEndlessScrollController(private val callback: OnLoadMoreScrollListener) : RecyclerView.OnScrollListener() {

    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var firstVisibleItemPosition: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            visibleItemCount = linearLayoutManager.childCount
            totalItemCount = callback.getTotalCount()
            firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
            if(!callback.isLoading() && linearLayoutManager.itemCount != totalItemCount &&
                (visibleItemCount + firstVisibleItemPosition) < totalItemCount)
                callback.onLoadMore()
        }
    }

    interface OnLoadMoreScrollListener {
        fun onLoadMore()
        fun getTotalCount(): Int
        fun isLoading(): Boolean
    }
}