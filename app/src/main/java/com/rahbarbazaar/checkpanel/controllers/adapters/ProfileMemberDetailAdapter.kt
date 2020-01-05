package com.rahbarbazaar.checkpanel.controllers.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.viewholders.ProfileMemberDetailViewHolder
import com.rahbarbazaar.checkpanel.models.profile.MemberDetailObj

class ProfileMemberDetailAdapter (private val memberDetailObj: List<MemberDetailObj>, val context: Context) :
        RecyclerView.Adapter<ProfileMemberDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ProfileMemberDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_profile_member_detail, parent, false)
        return ProfileMemberDetailViewHolder(view,context)
    }

    override fun onBindViewHolder(holder: ProfileMemberDetailViewHolder, position: Int) {
        val model = memberDetailObj[position]
        holder.bindUserData(model)

    }

    override fun getItemCount(): Int {
        return  memberDetailObj.size
    }
}