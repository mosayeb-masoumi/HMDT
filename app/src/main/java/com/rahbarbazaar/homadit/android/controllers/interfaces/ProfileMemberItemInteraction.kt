package com.rahbarbazaar.homadit.android.controllers.interfaces

import com.rahbarbazaar.homadit.android.models.profile.MemberDetail


interface ProfileMemberItemInteraction {
   fun profileMemberitemOnClicked(member_detail: MemberDetail, position: Int)
}