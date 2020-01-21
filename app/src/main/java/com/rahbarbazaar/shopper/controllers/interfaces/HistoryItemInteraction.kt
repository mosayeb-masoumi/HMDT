package com.rahbarbazaar.shopper.controllers.interfaces

import com.rahbarbazaar.shopper.models.history.History

interface HistoryItemInteraction {

    fun historyListOnClicked(model: History, btn_title: String)
}