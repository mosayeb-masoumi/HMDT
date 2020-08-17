package com.rahbarbazaar.homadit.android.controllers.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.viewholders.ProfileFamilyViewHolder
import com.rahbarbazaar.homadit.android.models.profile.Family

class ProfileFamilyAdapter (private val family: List<Family>, val context: Context) :
        RecyclerView.Adapter<ProfileFamilyViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ProfileFamilyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_profile_family, parent, false)
        return ProfileFamilyViewHolder(view,context)
    }

    override fun onBindViewHolder(holder: ProfileFamilyViewHolder, position: Int) {
        val model = family[position]
        holder.bindUserData(model)
    }

    override fun getItemCount(): Int {
        return family.size
    }
}