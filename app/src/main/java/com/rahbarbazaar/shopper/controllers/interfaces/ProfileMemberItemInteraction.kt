package com.rahbarbazaar.shopper.controllers.interfaces

import com.rahbarbazaar.shopper.models.profile.MemberDetail


interface ProfileMemberItemInteraction {
   fun profileMemberitemOnClicked(member_detail: MemberDetail, position: Int)
}