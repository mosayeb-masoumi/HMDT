package com.rahbarbazaar.checkpanel.controllers.interfaces

import com.rahbarbazaar.checkpanel.models.history.History

interface HistoryItemInteraction {

    fun historyListOnClicked(model: History, btn_title: String)
}