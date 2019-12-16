package com.rahbarbaazar.checkpanel.controllers.interfaces

import com.rahbarbaazar.checkpanel.models.barcodlist.BarcodeData

interface BarcodeItemInteraction {

    fun barcodeListOnClicked(model: BarcodeData, state: String)
}