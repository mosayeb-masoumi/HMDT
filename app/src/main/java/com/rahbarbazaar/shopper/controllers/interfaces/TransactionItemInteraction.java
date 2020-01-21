package com.rahbarbazaar.shopper.controllers.interfaces;

import com.rahbarbazaar.shopper.models.transaction.Transaction;

public interface TransactionItemInteraction {

    void transactionItemOnClicked(Transaction model, int position);
}

