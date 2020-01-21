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



       /*    -------------------------------------------------------------------------------                  */

//    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MessageAdapter.ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_message, parent, false)
//        return MessageViewHolder(view)
//
//    }
//
//    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
//
//        val model = message[position]
//        holder.setUpViewHolder(model)
//    }
//
//    override fun getItemCount(): Int {
//
//        return message.size
//    }


//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        private val txtDate: TextView = itemView.findViewById(R.id.item_date_message)
//        private val txtTitle: TextView = itemView.findViewById(R.id.item_title_message)
//        private val txtBody: TextView = itemView.findViewById(R.id.sub_item_body_message)
//        private val img_open: ImageView = itemView.findViewById(R.id.img_open_item_message)
//        private val sub_item: LinearLayout = itemView.findViewById(R.id.ll_sub_item)
//        private val ll_root: LinearLayout = itemView.findViewById(R.id.ll_row_message)
//
//        fun setUpViewHolder(model: Message) {
//
//            txtDate.text =model.date
//            txtTitle.text = model.title
//            txtBody.text = model.body
//
//
//
////            // Get the state
//            val expanded = model.expanded
//            // Set the visibility based on state
//            sub_item.visibility = if (expanded!!) View.VISIBLE else View.GONE
//
//            if(!expanded){
//                img_open.background = ContextCompat.getDrawable(context, R.drawable.plus_blue)
//            }else{
//                img_open.background = ContextCompat.getDrawable(context, R.drawable.minus)
//            }
//
//
//            itemView.setOnClickListener {
//
//                val expanded = model.expanded
//                // Change the state
//                model.expanded = (!expanded!!)
//
//                notifyItemChanged(adapterPosition)
//            }
//
//        }
//
//
//    }

}