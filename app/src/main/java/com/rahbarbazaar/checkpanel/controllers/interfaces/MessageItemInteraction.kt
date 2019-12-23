package com.rahbarbazaar.checkpanel.controllers.interfaces


import com.rahbarbazaar.checkpanel.models.message.Message

interface MessageItemInteraction {

    fun messageListOnClicked(model: Message, position: Int, status:String)
}