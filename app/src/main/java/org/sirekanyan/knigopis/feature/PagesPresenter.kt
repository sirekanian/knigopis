package org.sirekanyan.knigopis.feature

import org.sirekanyan.knigopis.model.CurrentTab

interface PagesPresenter {
    fun onPageUpdated(tab: CurrentTab)
}