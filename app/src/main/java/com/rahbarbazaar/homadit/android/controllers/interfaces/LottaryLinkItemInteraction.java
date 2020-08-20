package com.rahbarbazaar.homadit.android.controllers.interfaces;

import com.rahbarbazaar.homadit.android.models.Lottary.ActiveLinkDetail;
import com.rahbarbazaar.homadit.android.models.Lottary.OldMeDetail;

public interface LottaryLinkItemInteraction {
    void pastLottaryItemOnClicked(ActiveLinkDetail model, int position);
}
