package com.sirekanyan.knigopis.feature

import android.view.View
import com.sirekanyan.knigopis.common.extensions.hide
import com.sirekanyan.knigopis.common.extensions.show
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_main.*

interface ProgressView {

    fun showProgress()
    fun hideProgress()
    fun hideSwipeRefresh()

    interface Callbacks {
        fun onRefreshSwiped()
    }

}

class ProgressViewImpl(
    override val containerView: View,
    callbacks: ProgressView.Callbacks
) : ProgressView,
    LayoutContainer {

    init {
        swipeRefresh.setOnRefreshListener(callbacks::onRefreshSwiped)
    }

    override fun showProgress() {
        if (!swipeRefresh.isRefreshing) {
            booksProgressBar.show()
        }
    }

    override fun hideProgress() {
        booksProgressBar.hide()
    }

    override fun hideSwipeRefresh() {
        swipeRefresh.isRefreshing = false
    }

}