package com.rahbarbazaar.checkpanel.controllers.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.interfaces.HistoryItemInteraction
import com.rahbarbazaar.checkpanel.controllers.viewholders.HistoryViewHolder
import com.rahbarbazaar.checkpanel.models.history.History

class HistoryAdapter(private val history: List<History>, val context: Context) :
        RecyclerView.Adapter<HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): HistoryViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_history, parent, false)
        return HistoryViewHolder(view,context)
    }


    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val model = history[position]
        holder.bindUserData(model)
        holder.setOnMessageListHolderListener(listener, model, position)
    }

    private var listener: HistoryItemInteraction? = null
    fun setListener(listener: HistoryItemInteraction) {
        this.listener = listener

    }

    override fun getItemCount(): Int {
        return history.size
    }
}