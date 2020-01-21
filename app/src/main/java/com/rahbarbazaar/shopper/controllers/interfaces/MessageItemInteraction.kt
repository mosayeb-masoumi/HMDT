package com.rahbarbazaar.shopper.controllers.interfaces


import com.rahbarbazaar.shopper.models.message.Message

interface MessageItemInteraction {

    fun messageListOnClicked(model: Message, position: Int, status:String)
}