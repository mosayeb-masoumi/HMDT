package com.rahbarbazaar.homadit.android.controllers.interfaces

import com.rahbarbazaar.homadit.android.models.history.History

interface HistoryItemInteraction {

    fun historyListOnClicked(model: History, btn_title: String)
}