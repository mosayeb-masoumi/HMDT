package com.rahbarbazaar.homadit.android.controllers.viewholders

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.interfaces.ProfileMemberItemInteraction
import com.rahbarbazaar.homadit.android.models.profile.MemberDetail

class ProfileMemberViewHolder (view: View, val context: Context) : RecyclerView.ViewHolder(view){

    private val txt_name: TextView = itemView.findViewById(R.id.txt_name_profile_member_item)
    private val txt_role: TextView = itemView.findViewById(R.id.txt_role_profile_member_item)
    private val imgview: ImageView = itemView.findViewById(R.id.img_plus_profile_member)

    @SuppressLint("SetTextI18n")
    fun bindUserData(model: MemberDetail) {
        txt_name.text = model.role+": "
        txt_role.text=model.name
        imgview.setImageDrawable(itemView.context.resources.getDrawable(R.drawable.info_icon))

    }

    fun setOnProfileMemberListHolderListener(listener: ProfileMemberItemInteraction?, model_detail: MemberDetail, position: Int) {

        itemView.setOnClickListener {
            listener?.profileMemberitemOnClicked(model_detail,position)
        }
    }
}