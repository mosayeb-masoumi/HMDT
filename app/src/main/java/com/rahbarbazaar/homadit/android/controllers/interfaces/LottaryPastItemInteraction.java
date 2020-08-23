package com.rahbarbazaar.homadit.android.controllers.interfaces;

import com.rahbarbazaar.homadit.android.models.Lottary.OldMeDetail;
import com.wang.avi.AVLoadingIndicatorView;

public interface LottaryPastItemInteraction {

    void pastLottaryItemOnClicked(OldMeDetail model, int position, AVLoadingIndicatorView avi);
}
