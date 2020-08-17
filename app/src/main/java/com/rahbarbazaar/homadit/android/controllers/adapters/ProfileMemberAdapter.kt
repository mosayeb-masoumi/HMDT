package com.rahbarbazaar.homadit.android.controllers.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.interfaces.ProfileMemberItemInteraction
import com.rahbarbazaar.homadit.android.controllers.viewholders.ProfileMemberViewHolder
import com.rahbarbazaar.homadit.android.models.profile.MemberDetail

class ProfileMemberAdapter (private val member_detail: List<MemberDetail>, val context: Context) :
        RecyclerView.Adapter<ProfileMemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ProfileMemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_profile_member, parent, false)
        return ProfileMemberViewHolder(view, context)
    }


    override fun onBindViewHolder(holder: ProfileMemberViewHolder, position: Int) {
        val model = member_detail[position]
        holder.bindUserData(model)
        holder.setOnProfileMemberListHolderListener(listener, model, position)
    }

    private var listener: ProfileMemberItemInteraction? = null
    fun setListener(listener: ProfileMemberItemInteraction) {
        this.listener = listener

    }

    override fun getItemCount(): Int {
        return member_detail.size
    }
}