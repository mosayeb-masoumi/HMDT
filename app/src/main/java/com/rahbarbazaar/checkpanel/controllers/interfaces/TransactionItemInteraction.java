package com.rahbarbazaar.checkpanel.controllers.interfaces;

import com.rahbarbazaar.checkpanel.models.transaction.Transaction;

public interface TransactionItemInteraction {

    void transactionItemOnClicked(Transaction model, int position);
}

