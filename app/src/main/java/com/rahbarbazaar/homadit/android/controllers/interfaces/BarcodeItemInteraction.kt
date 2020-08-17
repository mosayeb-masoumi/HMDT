package com.rahbarbazaar.homadit.android.controllers.interfaces

import android.app.AlertDialog
import com.rahbarbazaar.homadit.android.models.barcodlist.Barcode
import com.rahbarbazaar.homadit.android.models.barcodlist.BarcodeData

interface BarcodeItemInteraction {

    fun barcodeListOnClicked(model: BarcodeData, position: Int, barcode: Barcode, dialog: AlertDialog)
}