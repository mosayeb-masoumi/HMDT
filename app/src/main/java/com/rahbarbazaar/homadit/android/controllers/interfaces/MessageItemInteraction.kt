package com.rahbarbazaar.homadit.android.controllers.interfaces


import com.rahbarbazaar.homadit.android.models.message.Message

interface MessageItemInteraction {

    fun messageListOnClicked(model: Message, position: Int, status:String)
}