package org.sirekanyan.knigopis.feature

import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.extensions.hide
import org.sirekanyan.knigopis.common.extensions.show
import org.sirekanyan.knigopis.databinding.ActivityMainBinding

interface ProgressView {

    fun showProgress()
    fun hideProgress()
    fun hideSwipeRefresh()

    interface Callbacks {
        fun onRefreshSwiped()
    }

}

class ProgressViewImpl(
    binding: ActivityMainBinding,
    callbacks: ProgressView.Callbacks,
) : ProgressView {

    private val resources = binding.root.resources
    private val swipeRefresh = binding.swipeRefresh
    private val booksProgressBar = binding.booksProgressBar

    private val startOffset = resources.getDimensionPixelSize(R.dimen.swipe_refresh_start_offset)
    private val endOffset = resources.getDimensionPixelSize(R.dimen.swipe_refresh_end_offset)

    init {
        swipeRefresh.setProgressViewOffset(true, startOffset, endOffset)
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