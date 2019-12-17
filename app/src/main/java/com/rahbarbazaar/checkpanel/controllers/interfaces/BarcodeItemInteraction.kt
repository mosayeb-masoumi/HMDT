package com.rahbarbazaar.checkpanel.controllers.interfaces

import com.rahbarbazaar.checkpanel.models.barcodlist.BarcodeData

interface BarcodeItemInteraction {

    fun barcodeListOnClicked(model: BarcodeData, state: String)
}