package com.rahbarbazaar.shopper.controllers.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

import android.view.ViewGroup
import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.shopper.controllers.interfaces.MessageItemInteraction
import com.rahbarbazaar.shopper.controllers.viewholders.MessageViewHolder

import com.rahbarbazaar.shopper.models.message.Message


class MessageAdapter(private val message: List<Message>, val context: Context) :
        RecyclerView.Adapter<MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_message, parent, false)
        return MessageViewHolder(view,context)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val model = message[position]
        holder.bindUserData(model)
        holder.setOnMessageListHolderListener(listener, model, position)
    }

    private var listener: MessageItemInteraction? = null
    fun setListener(listener: MessageItemInteraction) {
        this.listener = listener

    }

    override fun getItemCount(): Int {
        return message.size
    }

}