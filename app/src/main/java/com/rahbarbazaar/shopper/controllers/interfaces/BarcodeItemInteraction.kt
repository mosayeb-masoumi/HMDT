package com.rahbarbazaar.shopper.controllers.interfaces

import android.app.AlertDialog
import com.rahbarbazaar.shopper.models.barcodlist.Barcode
import com.rahbarbazaar.shopper.models.barcodlist.BarcodeData

interface BarcodeItemInteraction {

    fun barcodeListOnClicked(model: BarcodeData, position: Int, barcode: Barcode, dialog: AlertDialog)
}