package com.rahbarbazaar.homadit.android.controllers.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.interfaces.HistoryItemInteraction
import com.rahbarbazaar.homadit.android.controllers.viewholders.HistoryViewHolder
import com.rahbarbazaar.homadit.android.models.history.History

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