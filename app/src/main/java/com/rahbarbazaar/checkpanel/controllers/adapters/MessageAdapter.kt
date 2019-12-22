package com.rahbarbazaar.checkpanel.controllers.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.viewholders.BarcodeListViewHolder

import com.rahbarbazaar.checkpanel.models.message.Message


@Suppress("NAME_SHADOWING")
class MessageAdapter(private val message: List<Message>, val context: Context) :
        RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MessageAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_message, parent, false)
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {

        val model = message[position]
        holder.setUpViewHolder(model)



    }

    override fun getItemCount(): Int {

        return message.size
    }


//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        private val txtDate: TextView = itemView.findViewById(R.id.item_date_message)
//        private val txtTitle: TextView = itemView.findViewById(R.id.item_title_message)
//        private val txtBody: TextView = itemView.findViewById(R.id.sub_item_body_message)
//        private val img_close: ImageView = itemView.findViewById(R.id.img_close_item_message)
//        private val img_open: ImageView = itemView.findViewById(R.id.img_open_item_message)
//        private val ll_sub: LinearLayout = itemView.findViewById(R.id.ll_sub_item)
//        private val ll_root: LinearLayout = itemView.findViewById(R.id.ll_row_message)
//
//
//    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtDate: TextView = itemView.findViewById(R.id.item_date_message)
        private val txtTitle: TextView = itemView.findViewById(R.id.item_title_message)
        private val txtBody: TextView = itemView.findViewById(R.id.sub_item_body_message)
        private val img_open: ImageView = itemView.findViewById(R.id.img_open_item_message)
        private val sub_item: LinearLayout = itemView.findViewById(R.id.ll_sub_item)
        private val ll_root: LinearLayout = itemView.findViewById(R.id.ll_row_message)

        fun setUpViewHolder(model: Message) {

            txtDate.text =model.date
            txtTitle.text = model.title
            txtBody.text = model.body



//            // Get the state
            val expanded = model.expanded
            // Set the visibility based on state
            sub_item.visibility = if (expanded!!) View.VISIBLE else View.GONE

            if(!expanded){
                img_open.background = ContextCompat.getDrawable(context, R.drawable.plus)
            }else{
                img_open.background = ContextCompat.getDrawable(context, R.drawable.minus)
            }


            img_open.setOnClickListener {

                val expanded = model.expanded
                // Change the state
                model.expanded = (!expanded!!)

                notifyItemChanged(adapterPosition)
            }

        }


    }

}