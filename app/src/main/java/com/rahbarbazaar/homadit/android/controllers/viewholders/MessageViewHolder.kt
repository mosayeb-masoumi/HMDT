package com.rahbarbazaar.homadit.android.controllers.viewholders

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.interfaces.MessageItemInteraction
import com.rahbarbazaar.homadit.android.models.message.Message

class MessageViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    private val txtDate: TextView = itemView.findViewById(R.id.item_date_message)
    private val txtTitle: TextView = itemView.findViewById(R.id.item_title_message)
    private val txtBody: TextView = itemView.findViewById(R.id.sub_item_body_message)
    private val img_open: ImageView = itemView.findViewById(R.id.img_open_item_message)
    private val sub_item: LinearLayout = itemView.findViewById(R.id.ll_sub_item)
    private val ll_root: LinearLayout = itemView.findViewById(R.id.ll_row_message)


    @Suppress("DEPRECATION")
    fun bindUserData(model: Message) {

        txtDate.text = model.date
        txtTitle.text = model.title
        txtBody.text = model.body

            // Get the state
            val expanded = model.expanded
            // Set the visibility based on state
            sub_item.visibility = if (expanded!!) View.VISIBLE else View.GONE

            if(!expanded){
                if(model.status.equals("seen")){
                    img_open.background = ContextCompat.getDrawable(context, R.drawable.plus_blue)

                }else if(model.status.equals("unseen")){
                    img_open.background = ContextCompat.getDrawable(context, R.drawable.plus_blue_light)
                }

            }else if(expanded){
                if(model.status.equals("seen")){
                    img_open.background = ContextCompat.getDrawable(context, R.drawable.minus_blue)
                }else if(model.status.equals("unseen")){
                    img_open.background = ContextCompat.getDrawable(context, R.drawable.minus_blue_light)
                }
            }

           if(model.status.equals("seen")){
               ll_root.background = ContextCompat.getDrawable(context, R.drawable.activelist_rv_bg)
               txtDate.setTextColor(context.resources.getColor(R.color.blue_dark2))
               txtTitle.setTextColor(context.resources.getColor(R.color.blue_dark2))
               txtBody.setTextColor(context.resources.getColor(R.color.blue))
           }else if(model.status.equals("unseen")){
               ll_root.background = ContextCompat.getDrawable(context, R.drawable.unseen_message_bg)
               txtDate.setTextColor(context.resources.getColor(R.color.blue_light))
               txtTitle.setTextColor(context.resources.getColor(R.color.blue_light))
               txtDate.setTextColor(context.resources.getColor(R.color.blue_light))
               txtBody.setTextColor(context.resources.getColor(R.color.blue))
           }

    }

    fun setOnMessageListHolderListener(listener: MessageItemInteraction?, model: Message, position: Int) {


        itemView.setOnClickListener {

            // to check just unread messages Id send to server
            var status :String? = model.status

            //to change background to seen state while clicking(must be before notifyItemChanged() method)
            model.status = "seen"

            val expanded = model.expanded
            // Change the state
            model.expanded = (!expanded!!)
            listener?.messageListOnClicked(model,position, status!!)

        }
    }
}