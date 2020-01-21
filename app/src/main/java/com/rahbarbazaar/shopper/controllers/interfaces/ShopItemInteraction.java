package com.rahbarbazaar.shopper.controllers.interfaces;

import com.rahbarbazaar.shopper.models.shop.ShopCenterModel;


public interface ShopItemInteraction {
    void shopItemOnClicked(ShopCenterModel model, int position);
}
