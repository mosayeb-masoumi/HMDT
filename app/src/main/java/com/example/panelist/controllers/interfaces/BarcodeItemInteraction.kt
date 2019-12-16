package com.example.panelist.controllers.interfaces

import com.example.panelist.models.barcodlist.BarcodeData

interface BarcodeItemInteraction {

    fun barcodeListOnClicked(model: BarcodeData, state: String)
}