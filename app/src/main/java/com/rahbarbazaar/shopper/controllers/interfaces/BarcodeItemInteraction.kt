package com.rahbarbazaar.shopper.controllers.interfaces

import com.rahbarbazaar.shopper.models.barcodlist.BarcodeData

interface BarcodeItemInteraction {

    fun barcodeListOnClicked(model: BarcodeData, position: Int)
}